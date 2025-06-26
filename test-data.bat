@echo off
cd /d "d:\GITHUB\ÁGORA\Proyecto Personal\agora_back"

echo ============================================
echo       ÁGORA BACKEND - PRUEBA DE DATOS
echo ============================================
echo 🗄️  Probando carga de datos iniciales...
echo ============================================
echo.

java -jar target\agora_web-0.0.1-SNAPSHOT.jar --kafka.enabled=false

pause
