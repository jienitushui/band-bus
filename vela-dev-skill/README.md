<div align="center">

# VelaDev.skill

> *「不用翻文档，直接问。」*

[![Skills](https://img.shields.io/badge/skills.sh-Compatible-green)](https://skills.sh)
[![License: AGPL-3.0](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)
[![npm](https://img.shields.io/npm/v/vela-dev-skill)](https://www.npmjs.com/package/vela-dev-skill)

<br>

**一个面向 Xiaomi Vela / QuickApp 的本地文档检索 Skill：把官方文档切片进向量库，问一句就返回最相关的原文片段。**


<br>

[看效果](#效果示例) · [安装](#安装) · [它能查什么](#它能查什么) · [工作原理](#工作原理) · [仓库结构](#仓库结构)

</div>

---

## 效果示例

```text
用户      ❯ 怎么创建 vela 项目？

VelaDev   ❯ Source: creat-project.md
           AIoT-IDE 提供 Xiaomi Vela JS 应用项目的新建和管理功能：
           文件 -> 新建项目 -> 选择模板 -> 下一步 -> 填项目名和目录 -> 创建
```

```text
用户      ❯ IDE 里怎么新建模拟器？

VelaDev   ❯ Source: create-emulator.md
           设备管理页 -> 新建 -> 选择镜像(默认 vela-miwear-watch-5.0) -> 输入名称 -> 新建
```

---

## 安装

### 只作为命令行检索工具使用（推荐）

```bash
npx vela-dev-skill@latest "怎么创建vela项目"
```

### 作为 Claude Code / skills.sh Skill 安装

> 说明：这里的 `skills` 是 skills.sh 的安装器，所以首次执行会提示安装 `skills@...`，这是正常现象。

```bash
npx --yes skills@latest add CheongSzesuen/VelaDev.skill
```

### 给 Codex 安装（本地 Skill）
```bash
mkdir -p ~/.codex/skills/veladev

cat > ~/.codex/skills/veladev/SKILL.md << 'EOF'
---
name: veladev
description: Use when answering Xiaomi Vela development questions, including project creation, IDE/tooling, components, APIs, guides, and troubleshooting.
---

# VelaDev Skill

Trigger this skill when the user asks about **Xiaomi Vela OS development**.

## Action
Execute the following command to retrieve relevant documentation context:

npx vela-dev-skill@latest "<user question>"

## Response Guidelines
1. **Language**: Always answer the user in **Simplified Chinese**.
2. **Content**: Use the retrieved documentation snippets from the CLI output as the primary source of truth.
3. **Style**: Be concise and practical. Focus on code examples, steps, or API usage.
4. **Citation**: If helpful, mention the source file path provided by the CLI.
EOF
```

---

## 它能查什么

覆盖 Xiaomi Vela 相关文档内容（以仓库 `docs/` 中的文档为准），常见包括：

- Vela JS 应用开发与 API
- QuickApp 项目与模板
- AIoT-IDE / VelaDev 的运行、调试、打包、发布
- 模拟器创建、运行与设备管理
- 多屏适配、组件、布局与常见 UI/交互细节

---

## 工作原理

1. `docs/` 维护一份可离线读取的文档快照（通过 submodule 引入）。
2. `run_build.py` / `src/veladev/build_index.py` 将 Markdown 切片并构建 Chroma 向量库。
3. `vela-dev-skill` CLI 启动本地 Python 服务并对你的问题做相似度检索，返回 Top-K 原文片段（默认 3 条）。

技术要点：

- Embedding 模型：`BAAI/bge-small-zh-v1.5`（`FastEmbedEmbeddings`）
- 向量库：Chroma（`langchain_community.vectorstores.Chroma`）
- 文档切片：按 Markdown 标题层级切分（`MarkdownHeaderTextSplitter`）

---

## 仓库结构

```text
VelaDev.skill/
├── SKILL.md            # skills.sh / Claude Code Skill 入口
├── cli/                # npm 包（vela-dev-skill），含本地向量库与 Python 后端
├── docs/               # 文档 submodule（快照 + 更新工作流）
├── src/veladev/        # 构建索引、检索服务等 Python 代码
├── scripts/            # 文档抓取与排版修复脚本
└── run_build.py         # 构建向量库入口
```

---

## 构建 & 发布

### 构建向量库

```bash
# 常规构建
python run_build.py

# 低内存模式（batch 调小，适合配置较低的电脑）
python run_build.py --batch-size 4 --embed-batch-size 4
```

构建前先激活虚拟环境并安装依赖：

```bash
source venv/bin/activate
pip install -r requirements.txt
```

### 发布 npm 包

```bash
# 1. 修改版本号
cd cli
# 手动编辑 package.json 里的 "version" 字段
# 或使用 npm version 自动更新：
# npm version patch   # 1.0.0 → 1.0.1（小修小改）
# npm version minor   # 1.0.0 → 1.1.0（新增功能）
# npm version major   # 1.0.0 → 2.0.0（不兼容改动）

# 2. 确保向量库已构建（doc_vector_db/ 存在且最新）
python ../run_build.py

# 3. 发布
npm publish
```
