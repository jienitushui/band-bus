import os
import sys
import re
import shutil
import argparse

CURRENT_FILE_DIR = os.path.dirname(os.path.abspath(__file__))
SRC_DIR = os.path.dirname(CURRENT_FILE_DIR)
PROJECT_ROOT = os.path.dirname(SRC_DIR)

DEFAULT_DOCS_PATH = os.path.join(PROJECT_ROOT, "docs", "docs")
DEFAULT_DB_PATH = os.path.join(PROJECT_ROOT, "doc_vector_db")

from langchain_text_splitters import MarkdownHeaderTextSplitter
from langchain_community.vectorstores import Chroma
from langchain_community.embeddings import FastEmbedEmbeddings

try:
    from rich.progress import (
        Progress, SpinnerColumn, TextColumn, BarColumn,
        TaskProgressColumn, TimeRemainingColumn,
    )
    HAS_RICH = True
except ImportError:
    HAS_RICH = False


def clean_markdown_images(text):
    pattern = r'!\[.*?\]\(.*?\)'
    return re.sub(pattern, '', text)


def process_docs(root_dir):
    docs = []
    headers_to_split_on = [("#", "Header 1"), ("##", "Header 2"), ("###", "Header 3")]
    splitter = MarkdownHeaderTextSplitter(headers_to_split_on=headers_to_split_on)

    print(f"Scanning documents in: {root_dir}")
    found_files = []

    for dirpath, _, filenames in os.walk(root_dir):
        if 'images' in dirpath:
            continue
        for filename in filenames:
            if not filename.endswith('.md'):
                continue
            filepath = os.path.join(dirpath, filename)
            found_files.append(filepath)
            try:
                with open(filepath, 'r', encoding='utf-8') as f:
                    content = f.read()
                content = clean_markdown_images(content)
                lang = "zh" if "/zh/" in filepath else "en"
                meta = {"source": filepath, "lang": lang}
                chunks = splitter.split_text(content)
                for chunk in chunks:
                    chunk.metadata.update(meta)
                    docs.append(chunk)
            except Exception as e:
                print(f"Error processing {filepath}: {e}")

    print(f"Total .md files found: {len(found_files)}")
    if found_files:
        print("First 5 files:")
        for f in found_files[:5]:
            print(f"  - {f}")

    return docs


def build_database(output_dir, docs_root, batch_size=16, embed_batch_size=8):
    print("Processing and splitting documents...")
    all_chunks = process_docs(docs_root)

    if not all_chunks:
        print("No documents found to index!")
        return

    total_chunks = len(all_chunks)
    print(f"Split into {total_chunks} chunks.")

    print("Initializing embeddings...")
    embeddings = FastEmbedEmbeddings(
        model_name="intfloat/multilingual-e5-large",
        batch_size=embed_batch_size,
    )

    if os.path.exists(output_dir):
        shutil.rmtree(output_dir)

    print(f"Building vector database in batches of {batch_size}...")

    if HAS_RICH:
        progress = Progress(
            SpinnerColumn(),
            TextColumn("[progress.description]{task.description}"),
            BarColumn(),
            TaskProgressColumn(),
            TimeRemainingColumn(),
            transient=False,
        )
        with progress:
            task = progress.add_task("Embedding chunks...", total=total_chunks)
            db = None
            for i in range(0, total_chunks, batch_size):
                batch = all_chunks[i:i + batch_size]
                if db is None:
                    db = Chroma.from_documents(batch, embeddings, persist_directory=output_dir)
                else:
                    db.add_documents(batch)
                progress.update(task, advance=len(batch))
    else:
        db = None
        for i in range(0, total_chunks, batch_size):
            batch = all_chunks[i:i + batch_size]
            if db is None:
                db = Chroma.from_documents(batch, embeddings, persist_directory=output_dir)
            else:
                db.add_documents(batch)
            print(f"  Progress: {min(i + batch_size, total_chunks)}/{total_chunks} chunks")

    print(f"Done! Database saved to {output_dir}")


def main():
    parser = argparse.ArgumentParser(description="Build the Vela vector database")
    parser.add_argument("--batch-size", type=int, default=16,
                        help="Chunks per batch (default: 16, lower = less memory)")
    parser.add_argument("--embed-batch-size", type=int, default=8,
                        help="Internal embedding batch size (default: 8)")
    parser.add_argument("--docs-root", default=DEFAULT_DOCS_PATH)
    parser.add_argument("--output-dir", default=DEFAULT_DB_PATH)
    args = parser.parse_args()

    build_database(
        output_dir=args.output_dir,
        docs_root=args.docs_root,
        batch_size=args.batch_size,
        embed_batch_size=args.embed_batch_size,
    )


if __name__ == "__main__":
    main()
