@echo off
echo.
echo   InviteRoute ^— Build ^& Run
echo   ================================
echo.

if not exist bin mkdir bin

echo   Compiling...
javac -d bin src\*.java

if %errorlevel% neq 0 (
    echo.
    echo   Compilation failed. Make sure JDK is installed.
    echo   Download: https://adoptium.net/
    pause
    exit /b 1
)

echo   Compiled successfully!
echo.
echo   Running...
echo.
java -cp bin Main
pause
