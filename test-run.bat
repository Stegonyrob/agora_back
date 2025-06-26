@echo off
cd /d "d:\GITHUB\ÁGORA\Proyecto Personal\agora_back"

echo ============================================
echo       ÁGORA BACKEND - PRUEBA MYSQL
echo ============================================

mvn clean package -DskipTests
if %errorlevel% neq 0 (
    echo Error en compilación
    pause
    exit /b 1
)

echo Iniciando aplicación...
java -jar target\agora_web-0.0.1-SNAPSHOT.jar

pause
