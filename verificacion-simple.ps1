# Verificación rápida de inconsistencias - AGORA BACKEND
Write-Host "INICIANDO ESCANEO DE INCONSISTENCIAS - AGORA BACKEND" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

$ERRORS = 0
$WARNINGS = 0
$CHECKS = 0

Write-Host "Verificando estructura del proyecto..." -ForegroundColor Yellow

# 1. Verificar archivos críticos
Write-Host "1. Verificando archivos críticos..."
$CRITICAL_FILES = @(
    "src\main\java\de\stella\agora_web\config\SecurityConfiguration.java",
    "src\main\resources\application.properties",
    "src\main\resources\data.sql",
    "pom.xml"
)

foreach ($file in $CRITICAL_FILES) {
    $CHECKS++
    if (Test-Path $file) {
        Write-Host "   [OK] $file - EXISTE" -ForegroundColor Green
    } else {
        Write-Host "   [ERROR] $file - FALTA" -ForegroundColor Red
        $ERRORS++
    }
}

# 2. Verificar DTOs
Write-Host "2. Verificando DTOs..."
$CHECKS++
$DTO_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*DTO.java" -ErrorAction SilentlyContinue
if ($DTO_FILES) {
    Write-Host "   [OK] DTOs encontrados: $($DTO_FILES.Count)" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] No se encontraron DTOs" -ForegroundColor Red
    $ERRORS++
}

# 3. Verificar controladores
Write-Host "3. Verificando controladores..."
$CHECKS++
$CONTROLLER_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*Controller.java" -ErrorAction SilentlyContinue
if ($CONTROLLER_FILES) {
    Write-Host "   [OK] Controladores encontrados: $($CONTROLLER_FILES.Count)" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] No se encontraron controladores" -ForegroundColor Red
    $ERRORS++
}

# 4. Verificar servicios
Write-Host "4. Verificando servicios..."
$CHECKS++
$SERVICE_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*Service.java" -ErrorAction SilentlyContinue
$INTERFACE_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "I*Service.java" -ErrorAction SilentlyContinue

Write-Host "   Servicios encontrados: $($SERVICE_FILES.Count)"
Write-Host "   Interfaces encontradas: $($INTERFACE_FILES.Count)"

# 5. Verificar entidades JPA
Write-Host "5. Verificando entidades JPA..."
$CHECKS++
$JAVA_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*.java" -ErrorAction SilentlyContinue
$ENTITY_COUNT = 0

foreach ($file in $JAVA_FILES) {
    $content = Get-Content $file.FullName -Raw -ErrorAction SilentlyContinue
    if ($content -match "@Entity") {
        $ENTITY_COUNT++
    }
}

if ($ENTITY_COUNT -gt 0) {
    Write-Host "   [OK] Entidades encontradas: $ENTITY_COUNT" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] No se encontraron entidades JPA" -ForegroundColor Red
    $ERRORS++
}

# 6. Verificar configuración de seguridad
Write-Host "6. Verificando configuración de seguridad..."
$CHECKS++
$SECURITY_CONFIG = "src\main\java\de\stella\agora_web\config\SecurityConfiguration.java"
if (Test-Path $SECURITY_CONFIG) {
    Write-Host "   [OK] Configuración de seguridad - EXISTE" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] Configuración de seguridad - NO ENCONTRADA" -ForegroundColor Red
    $ERRORS++
}

# 7. Verificar tests
Write-Host "7. Verificando tests..."
$CHECKS++
$TEST_FILES = Get-ChildItem -Recurse -Path "src\test\java" -Filter "*Test.java" -ErrorAction SilentlyContinue
if ($TEST_FILES) {
    $TEST_COUNT = $TEST_FILES.Count
    Write-Host "   [OK] Tests encontrados: $TEST_COUNT" -ForegroundColor Green
    if ($TEST_COUNT -lt 5) {
        Write-Host "   [WARNING] Pocos tests - Considerar agregar más" -ForegroundColor Yellow
        $WARNINGS++
    }
} else {
    Write-Host "   [WARNING] No se encontraron tests" -ForegroundColor Yellow
    $WARNINGS++
}

# 8. Verificar pom.xml
Write-Host "8. Verificando dependencias en pom.xml..."
$CHECKS++
if (Test-Path "pom.xml") {
    $pomContent = Get-Content "pom.xml" -Raw
    $CRITICAL_DEPS = @(
        "spring-boot-starter-web",
        "spring-boot-starter-security", 
        "spring-boot-starter-data-jpa"
    )
    
    $foundDeps = 0
    foreach ($dep in $CRITICAL_DEPS) {
        if ($pomContent -match $dep) {
            $foundDeps++
        }
    }
    
    Write-Host "   [OK] Dependencias críticas encontradas: $foundDeps/$($CRITICAL_DEPS.Count)" -ForegroundColor Green
} else {
    Write-Host "   [ERROR] pom.xml no encontrado" -ForegroundColor Red
    $ERRORS++
}

# RESUMEN FINAL
Write-Host ""
Write-Host "RESUMEN DEL ESCANEO" -ForegroundColor Cyan
Write-Host "===================" -ForegroundColor Cyan
Write-Host "Verificaciones realizadas: $CHECKS"
Write-Host "Sin problemas: $($CHECKS - $ERRORS - $WARNINGS)" -ForegroundColor Green
Write-Host "Advertencias: $WARNINGS" -ForegroundColor Yellow
Write-Host "Errores críticos: $ERRORS" -ForegroundColor Red

Write-Host ""
if ($ERRORS -eq 0 -and $WARNINGS -eq 0) {
    Write-Host "TODO PERFECTO! El proyecto está listo para el siguiente paso." -ForegroundColor Green
} elseif ($ERRORS -eq 0) {
    Write-Host "Hay algunas advertencias, pero el proyecto está mayormente bien." -ForegroundColor Yellow
} else {
    Write-Host "HAY ERRORES CRITICOS que deben ser corregidos antes de continuar." -ForegroundColor Red
}

Write-Host ""
Write-Host "PROXIMOS PASOS:" -ForegroundColor Cyan
Write-Host "1. Corregir errores críticos encontrados"
Write-Host "2. Revisar advertencias y decidir si requieren acción"
Write-Host "3. Ejecutar tests completos"
Write-Host "4. Cambiar configuración de BD a persistente"
Write-Host "5. Verificar persistencia de datos"

Write-Host ""
Write-Host "Escaneo completado. Continuamos con el siguiente paso..." -ForegroundColor Green