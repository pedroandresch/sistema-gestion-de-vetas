#!/bin/bash
# Script de compilacion y ejecucion del Sistema de Gestion de Ventas

DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$DIR"

echo "=================================================="
echo "    Compilando Sistema de Gestion de Ventas...   "
echo "=================================================="

mkdir -p bin

javac -cp "lib/*" -d bin src/main/java/com/sistema/**/*.java

if [ $? -eq 0 ]; then
    echo "Compilacion exitosa. Iniciando aplicacion..."
    java -cp "bin:lib/*" com.sistema.Main
else
    echo "Error en la compilacion."
    exit 1
fi
