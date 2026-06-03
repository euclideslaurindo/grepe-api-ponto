@echo off
cd /d "%~dp0"
echo Compilando grepe-api...
call mvnw.cmd clean install
if errorlevel 1 (
    echo BUILD FALHOU.
    pause
    exit /b 1
)
echo BUILD OK.
pause
