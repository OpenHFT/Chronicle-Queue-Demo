#!/usr/bin/env bash

# Usage: ./list_files_asciidoc.sh [directory] > output.adoc
# If no directory is provided, the script defaults to the current directory.

DIR="${1:-.}"

cd $DIR
echo "= Directory Content"
echo

# Find all regular files recursively
find "pom.xml" "src" "*.adoc" -type f | grep -v out- | sort | while read -r file; do
    echo "== File: $file"
    echo "[source]"
    echo "----"
    cat "$file"
    echo "----"
    echo
done
