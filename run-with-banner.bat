@echo off
chcp 65001 > nul
cd /d "d:\GITHUB\ÁGORA\Proyecto Personal\agora_back"

echo ============================================
echo          INICIANDO ÁGORA BACKEND  
echo ============================================
echo 🚀 Ejecutando aplicación con logs completos...
echo ============================================
echo.

java -jar target\agora_web-0.0.1-SNAPSHOT.jar
