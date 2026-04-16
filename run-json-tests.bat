@echo off
echo ===============================================
echo    TESTS DE OPTIMIZACION JSON - AGORA WEB
echo ===============================================
echo.

echo 🚀 Selecciona que tests ejecutar:
echo.
echo 1. Test basico de suite
echo 2. Tests de Tags (endpoints problematicos)
echo 3. Tests de Posts
echo 4. Tests de Events
echo 5. Test completo de integracion
echo 6. Solo endpoints problematicos especificos
echo 7. Todos los tests JSON
echo 8. Compilar y verificar que no hay errores
echo.

set /p choice="Selecciona una opcion (1-8): "

if "%choice%"=="1" (
    echo.
    echo 📋 Ejecutando test basico de suite...
    mvn test -Dtest=JsonTestSuite
)

if "%choice%"=="2" (
    echo.
    echo 🏷️ Ejecutando tests de Tags...
    mvn test -Dtest=TagControllerJsonTest
)

if "%choice%"=="3" (
    echo.
    echo 📝 Ejecutando tests de Posts...
    mvn test -Dtest=PostControllerJsonTest
)

if "%choice%"=="4" (
    echo.
    echo 📅 Ejecutando tests de Events...
    mvn test -Dtest=EventControllerJsonTest
)

if "%choice%"=="5" (
    echo.
    echo 🔍 Ejecutando test completo de integracion...
    mvn test -Dtest=AllEndpointsJsonIntegrationTest
)

if "%choice%"=="6" (
    echo.
    echo ⚠️ Ejecutando solo endpoints problematicos...
    mvn test -Dtest=AllEndpointsJsonIntegrationTest#testSpecificProblematicEndpoints
)

if "%choice%"=="7" (
    echo.
    echo 🎯 Ejecutando TODOS los tests JSON...
    mvn test -Dtest="*JsonTest*"
)

if "%choice%"=="8" (
    echo.
    echo 🔨 Compilando proyecto...
    mvn clean compile -DskipTests
)

echo.
echo ✅ Ejecucion completada.
echo 📊 Revisa la salida anterior para ver los resultados detallados.
echo.
pause
