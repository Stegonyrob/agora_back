@echo off
echo.
echo ============================================
echo           ÁGORA CENTRO EDUCATIVO
echo ============================================
echo    🎓 Plataforma Educativa Interactiva
echo    📚 Sistema de Gestión de Aprendizaje  
echo ============================================
echo.

echo [1] Limpiando proyecto...
mvn clean -q

if %ERRORLEVEL% NEQ 0 (
    echo ❌ ERROR: No se pudo limpiar el proyecto
    echo 📋 Verifica que Maven esté instalado y configurado
    pause
    exit /b 1
)

echo ✅ Proyecto limpio exitosamente
echo.
echo [2] Compilando proyecto...
mvn compile -q

if %ERRORLEVEL% NEQ 0 (
    echo ❌ ERROR: No se pudo compilar el proyecto
    echo 📋 Revisa los errores de compilación arriba
    pause
    exit /b 1
)

echo ✅ Compilación exitosa
echo.
echo [3] Iniciando ÁGORA Backend...
echo.
echo ============================================
echo           CONFIGURACIÓN ACTIVA
echo ============================================
echo 🔧 Perfil: DESARROLLO (dev)
echo 🗄️  Base de datos: H2 (en memoria)
echo 🌐 Puerto: 8080
echo 🔐 CORS: Permitido para desarrollo
echo 📊 Logs: DEBUG activado
echo 🎨 Banner: ÁGORA Centro Educativo
echo.
echo 📡 Endpoints disponibles:
echo   • API Base: http://localhost:8080/api/v1
echo   • H2 Console: http://localhost:8080/h2-console
echo   • Swagger UI: http://localhost:8080/swagger-ui.html
echo   • Actuator: http://localhost:8080/actuator
echo ============================================
echo.
echo 🚀 Iniciando servidor Spring Boot...
echo 📋 Presiona Ctrl+C para detener el servidor
echo.
echo.

mvn spring-boot:run -Dspring.profiles.active=dev

echo.
echo La aplicación se ha detenido.
pause
