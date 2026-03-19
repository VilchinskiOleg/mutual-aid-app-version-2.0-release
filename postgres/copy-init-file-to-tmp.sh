#!/usr/bin/env bash
set -euo pipefail

# Usage: ./copy_to_postgres.sh <source-file>
SRC="init-db.sh"
DEST_DIR="/tmp/mutual-aid-app/postgres"
DEST_PATH="$DEST_DIR/$(basename "$SRC")"

# Create destination directory (and parents) if needed
mkdir -p "$DEST_DIR"

# Copy the file or dir (overwrites if exists)
cp -r "$SRC" "$DEST_PATH"

echo "Copied '$SRC' → '$DEST_PATH'"