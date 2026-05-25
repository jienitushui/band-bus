#!/usr/bin/env node

const axios = require('axios');
const chalk = require('chalk').default || require('chalk');
const { Command } = require('commander');
const { execSync, spawn } = require('child_process');
const crypto = require('crypto');
const fs = require('fs');
const path = require('path');
const which = require('which');

const PORT = 8000;
const CACHE_DIR = path.join(require('os').homedir(), '.veladev-cache');
const PACKAGE_DIR = __dirname;

if (!fs.existsSync(CACHE_DIR)) {
  fs.mkdirSync(CACHE_DIR, { recursive: true });
}

function syncFilesToCache() {
  const versionFile = path.join(CACHE_DIR, 'version.txt');
  const currentVersion = require('./package.json').version;
  
  let needsSync = false;
  if (!fs.existsSync(versionFile) || fs.readFileSync(versionFile, 'utf-8').trim() !== currentVersion) {
    needsSync = true;
  }
  
  if (!fs.existsSync(path.join(CACHE_DIR, 'doc_vector_db'))) {
    needsSync = true;
  }

  if (needsSync) {
    console.log(chalk.yellow('📦 Initializing local cache...'));
    console.log(chalk.gray('   Copying vector database...'));
    execSync(`cp -r "${path.join(PACKAGE_DIR, 'doc_vector_db')}" "${CACHE_DIR}/"`);
    
    console.log(chalk.gray('   Copying backend code...'));
    execSync(`cp -r "${path.join(PACKAGE_DIR, 'backend')}" "${CACHE_DIR}/"`);
    
    console.log(chalk.gray('   Copying scripts...'));
    execSync(`cp "${path.join(PACKAGE_DIR, 'server.py')}" "${CACHE_DIR}/"`);
    execSync(`cp "${path.join(PACKAGE_DIR, 'requirements.txt')}" "${CACHE_DIR}/"`);
    
    fs.writeFileSync(versionFile, currentVersion);
    console.log(chalk.green('✅ Cache ready.'));
  }
}

function ensureDependencies() {
  const venvPath = path.join(CACHE_DIR, 'venv');
  const reqPath = path.join(CACHE_DIR, 'requirements.txt');
  const statePath = path.join(CACHE_DIR, 'venv-state.json');
  const pythonPath = path.join(venvPath, 'bin', 'python');
  const pipPath = path.join(venvPath, 'bin', 'pip');
  const requirementsHash = crypto
    .createHash('sha256')
    .update(fs.readFileSync(reqPath))
    .digest('hex');
  const pythonVersion = execSync('python3 --version', { encoding: 'utf-8' }).trim();
  const expectedState = {
    pythonVersion,
    requirementsHash
  };
  
  if (fs.existsSync(venvPath) && fs.existsSync(pythonPath) && fs.existsSync(pipPath) && fs.existsSync(statePath)) {
    try {
      const currentState = JSON.parse(fs.readFileSync(statePath, 'utf-8'));
      if (
        currentState.pythonVersion === expectedState.pythonVersion &&
        currentState.requirementsHash === expectedState.requirementsHash
      ) {
        console.log(chalk.green('✅ Python environment ready (cached).'));
        return;
      }
    } catch (e) {
      console.log(chalk.gray('   Python environment cache metadata is invalid; rebuilding...'));
    }
  }

  console.log(chalk.yellow('🐍 Setting up Python environment...'));
  if (fs.existsSync(venvPath)) {
    console.log(chalk.gray('   Removing outdated virtual environment...'));
    fs.rmSync(venvPath, { recursive: true, force: true });
  }

  console.log(chalk.gray('   Creating fresh virtual environment...'));
  process.chdir(CACHE_DIR);
  execSync('python3 -m venv venv', { stdio: 'inherit' });
  
  console.log(chalk.gray('   Installing dependencies (may take 1-2 mins)...'));
  
  try {
    execSync(`${pipPath} install --no-cache-dir -r ${reqPath}`, { stdio: 'inherit' });
  } catch (e) {
    console.error(chalk.red('❌ Failed to install dependencies.'));
    throw e;
  }
  fs.writeFileSync(statePath, JSON.stringify(expectedState, null, 2));
  console.log(chalk.green('✅ Dependencies installed.'));
}

async function startServer() {
  try {
    await axios.get(`http://127.0.0.1:${PORT}/docs`, { timeout: 1000 });
    console.log(chalk.green('✅ Server ready (already running).'));
    return;
  } catch (e) {
    // Start a local server below.
  }

  console.log(chalk.yellow('🚀 Starting local server...'));
  process.chdir(CACHE_DIR);
  
  // Use absolute path for python in venv
  const pythonPath = path.join(CACHE_DIR, 'venv', 'bin', 'python');
  const logPath = path.join(CACHE_DIR, 'server.log');
  
  const env = {
    ...process.env,
    PYTHONPATH: `${CACHE_DIR}${path.delimiter}${process.env.PYTHONPATH || ''}`
  };

  fs.writeFileSync(logPath, '');
  const logFd = fs.openSync(logPath, 'a');
  const server = spawn(pythonPath, ['server.py'], {
    detached: true,
    stdio: ['ignore', logFd, logFd],
    env: env
  });
  server.unref();
  fs.closeSync(logFd);

  // Wait up to 45 seconds for server to be ready (model loading can be slow)
  let isReady = false;
  const maxRetries = 90; // 90 * 500ms = 45s
  
  for (let i = 0; i < maxRetries; i++) {
    try {
      await axios.get(`http://127.0.0.1:${PORT}/docs`, { timeout: 1000 });
      isReady = true;
      break;
    } catch (e) {
      await new Promise(r => setTimeout(r, 500));
      if (i % 20 === 0 && i > 0) console.log(chalk.gray(`   Waiting for server... (${i/2}s)`));
    }
  }

  if (!isReady) {
    console.error(chalk.red('\n⚠️ Server did not start in time.'));
    if (fs.existsSync(logPath)) {
      const serverLog = fs.readFileSync(logPath, 'utf-8').trim();
      if (serverLog) {
        console.error(chalk.yellow('\nServer log:'));
        console.error(serverLog.split('\n').slice(-40).join('\n'));
      }
    }
    console.error(chalk.yellow('Hint: Run the following commands to debug:'));
    console.error(chalk.cyan(`   cd ${CACHE_DIR}`));
    console.error(chalk.cyan('   source venv/bin/activate'));
    console.error(chalk.cyan('   python server.py'));
    throw new Error('Server startup failed.');
  }
  
  console.log(chalk.green('✅ Server ready.'));
}

const program = new Command();

program
  .name('vela-dev')
  .description('Vela Dev Docs RAG Skill')
  .argument('<question>', 'Your question')
  .action(async (question) => {
    try {
      try {
        which.sync('python3');
      } catch (e) {
        console.error(chalk.red('❌ Python 3 not found.'));
        process.exit(1);
      }

      syncFilesToCache();
      ensureDependencies();
      await startServer();

      console.log(chalk.blue(`\n🔎 Asking: "${question}"`));
      const res = await axios.post(`http://127.0.0.1:${PORT}/search`, {
        question: question,
        k: 3
      }, { timeout: 120000 });

      if (res.data.error) {
        console.error(chalk.red('Error:', res.data.error));
        return;
      }

      if (!res.data.results || res.data.results.length === 0) {
        console.log(chalk.yellow('No results found.'));
        return;
      }

      res.data.results.forEach((r, i) => {
        console.log(chalk.green(`\n--- Result ${i+1} ---`));
        console.log(chalk.gray(`Source: ${path.basename(r.source)}`));
        console.log(r.content.substring(0, 600) + (r.content.length > 600 ? '...' : ''));
      });

    } catch (err) {
      console.error(chalk.red('\n❌ Fatal Error:', err.message));
    }
  });

program.parse();
