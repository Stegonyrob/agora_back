@echo off
echo Compilando proyecto...
mvn clean package -DskipTests -q

if %ERRORLEVEL% NEQ 0 (
    echo Error en la compilación.
    pause
    exit /b 1
)

echo Configurando variables de entorno...

set ACCESS_TOKEN_PRIVATE_KEY_PATH=src/main/java/de/stella/agora_web/auth/keys/access-token-private.key
set ACCESS_TOKEN_PUBLIC_KEY_PATH=src/main/java/de/stella/agora_web/auth/keys/access-token-public.key
set REFRESH_TOKEN_PRIVATE_KEY_PATH=src/main/java/de/stella/agora_web/auth/keys/refresh-token-private.key
set REFRESH_TOKEN_PUBLIC_KEY_PATH=src/main/java/de/stella/agora_web/auth/keys/refresh-token-public.key

echo Iniciando Agora Backend (JAR)...
java -jar target\agora_web-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev

pause
