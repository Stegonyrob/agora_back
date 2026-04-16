@echo off
cd /d "d:\GITHUB\ÁGORA\Proyecto Personal\agora_back"

echo ============================================
echo          ÁGORA BACKEND - MYSQL
echo ============================================
echo 🗄️  Conectando a base de datos MySQL...
echo 📊 Base de datos: agora_db
echo 🌐 Host: localhost:3306
echo 🔕 Notificaciones: DESHABILITADAS
echo ============================================
echo.

echo [INFO] Compilando aplicación...
call mvn clean package -DskipTests

if %errorlevel% neq 0 (
    echo [ERROR] Falló la compilación
    pause
    exit /b 1
)

echo [INFO] Iniciando aplicación sin notificaciones...
java -jar target\agora_web-0.0.1-SNAPSHOT.jar --kafka.enabled=false

pause
