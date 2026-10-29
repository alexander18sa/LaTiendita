@echo off
chcp 65001 > nul
where mvn >nul 2>nul
if errorlevel 1 (
  echo No se encontro Maven. Abre el proyecto en NetBeans o instala Maven.
  pause
  exit /b 1
)
echo Ejecutando pruebas y compilacion de La Tiendita...
call mvn clean test
if errorlevel 1 goto error
call mvn package
if errorlevel 1 goto error
echo.
echo Ejecutando La Tiendita...
java -jar target\la-tiendita-1.0.jar
pause
exit /b 0
:error
echo.
echo La compilacion o las pruebas fallaron. Revisa el mensaje anterior.
pause
exit /b 1
