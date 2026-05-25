#!/usr/bin/env python3
import sys
import os
import argparse

sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

from veladev.build_index import build_database, DEFAULT_DB_PATH, DEFAULT_DOCS_PATH

if __name__ == "__main__":
    parser = argparse.ArgumentParser(description="Build the Vela vector database")
    parser.add_argument("--batch-size", type=int, default=16,
                        help="Chunks per batch (default: 16, lower = less memory)")
    parser.add_argument("--embed-batch-size", type=int, default=8,
                        help="Internal embedding batch size (default: 8)")
    args = parser.parse_args()

    print("Starting index build process...")
    build_database(
        output_dir=DEFAULT_DB_PATH,
        docs_root=DEFAULT_DOCS_PATH,
        batch_size=args.batch_size,
        embed_batch_size=args.embed_batch_size,
    )
    print("Index build completed.")
