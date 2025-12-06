#!/usr/bin/env bash
set -euo pipefail

# Usage: ./copy_to_postgres.sh <source-file>
SRC="nginx.conf"
DEST_DIR="/tmp/mutual-aid-app/nginx"
DEST_PATH="$DEST_DIR/$(basename "$SRC")"

# Create destination directory (and parents) if needed
mkdir -p "$DEST_DIR"

# Copy the file (overwrites if exists)
cp "$SRC" "$DEST_PATH"

echo "Copied '$SRC' → '$DEST_PATH'"