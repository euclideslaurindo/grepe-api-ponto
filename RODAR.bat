@echo off
cd /d "%~dp0"
echo ============================================
echo   GREPE-API - Compilar e iniciar (porta 8085)
echo ============================================
echo.
echo Requisitos: MySQL ligado, banco grepe_test configurado em application.properties
echo.

call mvnw.cmd -q clean install
if errorlevel 1 (
    echo.
    echo ERRO na compilacao. Veja a mensagem acima.
    pause
    exit /b 1
)

echo.
echo Compilacao OK. Iniciando servidor...
echo Acesse: http://localhost:8085/
echo Para parar: Ctrl+C
echo.

call mvnw.cmd spring-boot:run
