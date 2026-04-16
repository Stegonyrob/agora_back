@echo off
echo Iniciando Ágora Backend en modo debug...
echo.

cd /d "%~dp0"

echo Verificando archivo JAR...
if not exist "target\agora_web-0.0.1-SNAPSHOT.jar" (
    echo ERROR: No se encuentra el archivo JAR. Ejecutando mvn package...
    mvn clean package -DskipTests
    if errorlevel 1 (
        echo ERROR: Falló la compilación
        pause
        exit /b 1
    )
)

echo.
echo Iniciando aplicación Spring Boot...
echo.
echo [URL Backend]: http://localhost:8080
echo [URL Swagger]: http://localhost:8080/swagger-ui/index.html
echo [Endpoint Textos]: http://localhost:8080/api/v1/all/texts
echo.
echo Presiona Ctrl+C para detener el servidor
echo.

java -jar target\agora_web-0.0.1-SNAPSHOT.jar

pause
