@echo off
cd /d "d:\GITHUB\ÁGORA\Proyecto Personal\agora_back"

echo ============================================
echo    ÁGORA BACKEND - CON NOTIFICACIONES
echo ============================================
echo 📧 Activando sistema de notificaciones...
echo 🔄 Kafka habilitado para moderación
echo ============================================
echo.

echo [INFO] Compilando aplicación...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo [ERROR] Falló la compilación
    pause
    exit /b 1
)

echo [INFO] Iniciando aplicación con notificaciones activas...
java -jar target\agora_web-0.0.1-SNAPSHOT.jar --kafka.enabled=true

pause
