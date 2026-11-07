@echo off
setlocal enabledelayedexpansion
title Sistema de Gestion de Inventario y Ventas

:: Cambiar al directorio del script
cd /d "%~dp0"

echo ==========================================================
echo       SISTEMA DE GESTION DE INVENTARIO Y VENTAS
echo ==========================================================
echo.

:: 1. Verificar si Java esta instalado
where java >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] No se encontro Java instalado en el sistema o no esta en el PATH.
    echo Por favor instale Java JDK 17 o superior desde: https://adoptium.net/
    echo.
    pause
    exit /b 1
)

:: 2. Si ya existen los binarios precompilados, ejecutar directamente
if exist "bin\sistema.jar" (
    echo [OK] Iniciando aplicacion...
    java -cp "bin\sistema.jar;lib\*" com.sistema.Main
    if !errorlevel! equ 0 goto fin
)

:: 3. Si no existen, compilar con javac
echo [INFO] Compilando codigo fuente Java...
if not exist "bin" mkdir bin

where javac >nul 2>nul
if %errorlevel% neq 0 (
    echo [ERROR] No se encontro el compilador 'javac'. Asegurese de tener instalado el JDK.
    pause
    exit /b 1
)

javac -cp "lib/*" -encoding UTF-8 -d bin src/main/java/com/sistema/model/*.java src/main/java/com/sistema/persistence/*.java src/main/java/com/sistema/controller/*.java src/main/java/com/sistema/view/*.java src/main/java/com/sistema/*.java

if %errorlevel% neq 0 (
    echo [ERROR] Fallo la compilacion.
    pause
    exit /b 1
)

echo [OK] Compilacion exitosa. Iniciando...
java -cp "bin;lib/*" com.sistema.Main

:fin
if %errorlevel% neq 0 (
    echo.
    echo [AVISO] La aplicacion se cerro con codigo de salida %errorlevel%.
    pause
)
