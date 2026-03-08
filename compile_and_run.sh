#!/bin/bash
# ─────────────────────────────────────────────
# InviteRoute — Compile & Run Script
# Requirements: Java JDK 11 or higher
# ─────────────────────────────────────────────

echo ""
echo "  ╔══════════════════════════════════════╗"
echo "  ║  InviteRoute — Build & Run           ║"
echo "  ╚══════════════════════════════════════╝"
echo ""

# Create bin directory for .class files
mkdir -p bin

echo "  Compiling..."
javac -d bin src/*.java

if [ $? -ne 0 ]; then
  echo ""
  echo "  ❌ Compilation failed. Make sure JDK is installed."
  echo "     Download: https://adoptium.net/"
  exit 1
fi

echo "  ✅ Compiled successfully!"
echo ""
echo "  Running..."
echo ""
java -cp bin Main
