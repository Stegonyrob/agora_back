# 🔍 SCRIPT DE VERIFICACIÓN DE INCONSISTENCIAS - WINDOWS
# Para ejecutar el lunes antes de los cambios

Write-Host "INICIANDO ESCANEO DE INCONSISTENCIAS - AGORA BACKEND" -ForegroundColor Cyan
Write-Host "==================================================" -ForegroundColor Cyan

# Contadores
$ERRORS = 0
$WARNINGS = 0
$CHECKS = 0

Write-Host "📋 VERIFICANDO ESTRUCTURA DEL PROYECTO..." -ForegroundColor Yellow

# 1. Verificar archivos críticos
Write-Host "🔍 1. Verificando archivos críticos..."
$CRITICAL_FILES = @(
    "src\main\java\de\stella\agora_web\config\SecurityConfiguration.java",
    "src\main\resources\application.properties",
    "src\main\resources\data.sql",
    "pom.xml"
)

foreach ($file in $CRITICAL_FILES) {
    $CHECKS++
    if (Test-Path $file) {
        Write-Host "   ✅ $file - EXISTE" -ForegroundColor Green
    } else {
        Write-Host "   ❌ $file - FALTA" -ForegroundColor Red
        $ERRORS++
    }
}

# 2. Verificar DTOs
Write-Host "🔍 2. Verificando DTOs..."
$CHECKS++
$DTO_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*DTO.java" -ErrorAction SilentlyContinue
if ($DTO_FILES) {
    Write-Host "   ✅ DTOs encontrados: $($DTO_FILES.Count)" -ForegroundColor Green
    
    foreach ($dto in $DTO_FILES) {
        $content = Get-Content $dto.FullName -Raw
        if (-not ($content -match "@Data|@Getter|@Setter")) {
            Write-Host "   [WARNING] $($dto.Name) - Posible falta de anotaciones Lombok" -ForegroundColor Yellow
            $WARNINGS++
        }
    }
} else {
    Write-Host "   ❌ No se encontraron DTOs" -ForegroundColor Red
    $ERRORS++
}

# 3. Verificar controladores
Write-Host "🔍 3. Verificando controladores..."
$CHECKS++
$CONTROLLER_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*Controller.java" -ErrorAction SilentlyContinue
if ($CONTROLLER_FILES) {
    Write-Host "   ✅ Controladores encontrados: $($CONTROLLER_FILES.Count)" -ForegroundColor Green
    
    foreach ($controller in $CONTROLLER_FILES) {
        $content = Get-Content $controller.FullName -Raw
        if (-not ($content -match "@RestController" -or $content -match "@Controller")) {
            Write-Host "   [ERROR] $($controller.Name) - Falta anotación @RestController" -ForegroundColor Red
            $ERRORS++
        }
        if (-not ($content -match "@RequestMapping")) {
            Write-Host "   [WARNING] $($controller.Name) - Posible falta de @RequestMapping" -ForegroundColor Yellow
            $WARNINGS++
        }
    }
} else {
    Write-Host "   ❌ No se encontraron controladores" -ForegroundColor Red
    $ERRORS++
}

# 4. Verificar servicios
Write-Host "🔍 4. Verificando servicios..."
$CHECKS++
$SERVICE_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*Service.java" -ErrorAction SilentlyContinue
$INTERFACE_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "I*Service.java" -ErrorAction SilentlyContinue

Write-Host "   📊 Servicios encontrados: $($SERVICE_FILES.Count)"
Write-Host "   📊 Interfaces encontradas: $($INTERFACE_FILES.Count)"

# Verificar implementaciones
foreach ($interface in $INTERFACE_FILES) {
    $interfaceName = $interface.BaseName
    $implName = $interfaceName.Substring(1) + "Impl"
    $implExists = Get-ChildItem -Recurse -Path "src\main\java" -Filter "$implName.java" -ErrorAction SilentlyContinue
    
    if (-not $implExists) {
        Write-Host "   ⚠️  $($interface.Name) - No se encontró implementación $implName" -ForegroundColor Yellow
        $WARNINGS++
    }
}

# 5. Verificar entidades JPA
Write-Host "🔍 5. Verificando entidades JPA..."
$CHECKS++
$ENTITY_FILES = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*.java" -ErrorAction SilentlyContinue | 
    Where-Object { (Get-Content $_.FullName -Raw) -match "@Entity" }

if ($ENTITY_FILES) {
    Write-Host "   ✅ Entidades encontradas: $($ENTITY_FILES.Count)" -ForegroundColor Green
    
    foreach ($entity in $ENTITY_FILES) {
        $content = Get-Content $entity.FullName -Raw
        if (-not ($content -match "@Table|@Id")) {
            Write-Host "   ⚠️  $($entity.Name) - Verificar anotaciones JPA" -ForegroundColor Yellow
            $WARNINGS++
        }
    }
} else {
    Write-Host "   ❌ No se encontraron entidades JPA" -ForegroundColor Red
    $ERRORS++
}

# 6. Verificar configuración de seguridad
Write-Host "🔍 6. Verificando configuración de seguridad..."
$CHECKS++
$SECURITY_CONFIG = "src\main\java\de\stella\agora_web\config\SecurityConfiguration.java"
if (Test-Path $SECURITY_CONFIG) {
    $content = Get-Content $SECURITY_CONFIG -Raw
    
    if ($content -match "@EnableWebSecurity" -or $content -match "@Configuration") {
        Write-Host "   [OK] Configuración de seguridad - OK" -ForegroundColor Green
    } else {
        Write-Host "   ❌ Configuración de seguridad - Faltan anotaciones" -ForegroundColor Red
        $ERRORS++
    }
    
    if ($content -match "jwtAuthenticationConverter|JwtDecoder") {
        Write-Host "   ✅ Configuración JWT - OK" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  Configuración JWT - Verificar implementación" -ForegroundColor Yellow
        $WARNINGS++
    }
} else {
    Write-Host "   ❌ Archivo de configuración de seguridad no encontrado" -ForegroundColor Red
    $ERRORS++
}

# 7. Verificar tests
Write-Host "🔍 7. Verificando tests..."
$CHECKS++
$TEST_FILES = Get-ChildItem -Recurse -Path "src\test\java" -Filter "*Test.java" -ErrorAction SilentlyContinue
if ($TEST_FILES) {
    $TEST_COUNT = $TEST_FILES.Count
    Write-Host "   ✅ Tests encontrados: $TEST_COUNT" -ForegroundColor Green
    if ($TEST_COUNT -lt 5) {
        Write-Host "   ⚠️  Pocos tests - Considerar agregar más" -ForegroundColor Yellow
        $WARNINGS++
    }
} else {
    Write-Host "   ⚠️  No se encontraron tests" -ForegroundColor Yellow
    $WARNINGS++
}

# 8. Verificar dependencias en pom.xml
Write-Host "🔍 8. Verificando dependencias en pom.xml..."
$CHECKS++
if (Test-Path "pom.xml") {
    $pomContent = Get-Content "pom.xml" -Raw
    $CRITICAL_DEPS = @(
        "spring-boot-starter-web",
        "spring-boot-starter-security", 
        "spring-boot-starter-data-jpa",
        "spring-boot-starter-oauth2-resource-server"
    )
    
    foreach ($dep in $CRITICAL_DEPS) {
        if ($pomContent -match $dep) {
            Write-Host "   ✅ $dep - OK" -ForegroundColor Green
        } else {
            Write-Host "   ❌ $dep - FALTA" -ForegroundColor Red
            $ERRORS++
        }
    }
} else {
    Write-Host "   ❌ pom.xml no encontrado" -ForegroundColor Red
    $ERRORS++
}

# 9. Verificar archivos de configuración
Write-Host "🔍 9. Verificando archivos de configuración..."
$CHECKS++
$CONFIG_FILES = @(
    "src\main\resources\application.properties",
    "src\main\resources\application-dev.properties",  
    "src\main\resources\application-prod.properties"
)

foreach ($config in $CONFIG_FILES) {
    if (Test-Path $config) {
        Write-Host "   ✅ $config - EXISTE" -ForegroundColor Green
        
        if ($config -eq "src\main\resources\application.properties") {
            $content = Get-Content $config -Raw
            if (-not ($content -match "spring.jpa.hibernate.ddl-auto")) {
                Write-Host "   ⚠️  Falta configuración DDL en $config" -ForegroundColor Yellow
                $WARNINGS++
            }
        }
    } else {
        Write-Host "   ⚠️  $config - NO EXISTE" -ForegroundColor Yellow
        $WARNINGS++
    }
}

# 10. Verificar estructura de directorios
Write-Host "🔍 10. Verificando estructura de directorios..."
$CHECKS++
$REQUIRED_DIRS = @(
    "src\main\java\de\stella\agora_web",
    "src\main\resources",
    "src\test\java",
    "target"
)

foreach ($dir in $REQUIRED_DIRS) {
    if (Test-Path $dir) {
        Write-Host "   ✅ $dir - OK" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  $dir - NO EXISTE" -ForegroundColor Yellow
        $WARNINGS++
    }
}

# RESUMEN FINAL
Write-Host ""
Write-Host "🎯 RESUMEN DEL ESCANEO" -ForegroundColor Cyan
Write-Host "=====================" -ForegroundColor Cyan
Write-Host "📋 Verificaciones realizadas: $CHECKS"
Write-Host "✅ Sin problemas: $($CHECKS - $ERRORS - $WARNINGS)" -ForegroundColor Green
Write-Host "⚠️  Advertencias: $WARNINGS" -ForegroundColor Yellow  
Write-Host "❌ Errores críticos: $ERRORS" -ForegroundColor Red

Write-Host ""
if ($ERRORS -eq 0 -and $WARNINGS -eq 0) {
    Write-Host "🎉 ¡TODO PERFECTO! El proyecto está listo para el siguiente paso." -ForegroundColor Green
} elseif ($ERRORS -eq 0) {
    Write-Host "⚠️  Hay algunas advertencias, pero el proyecto está mayormente bien." -ForegroundColor Yellow
} else {
    Write-Host "🚨 HAY ERRORES CRÍTICOS que deben ser corregidos antes de continuar." -ForegroundColor Red
}

Write-Host ""
Write-Host "📝 PRÓXIMOS PASOS PARA EL LUNES:" -ForegroundColor Cyan
Write-Host "1. Corregir errores críticos encontrados"
Write-Host "2. Revisar advertencias y decidir si requieren acción"
Write-Host "3. Ejecutar tests completos"  
Write-Host "4. Cambiar configuración de BD a persistente"
Write-Host "5. Verificar persistencia de datos"

Write-Host ""
Write-Host "🏁 Escaneo completado. ¡Revisa los resultados y prepárate para el lunes!" -ForegroundColor Green

# Pausar para que el usuario pueda leer los resultados
Read-Host "Presiona Enter para continuar..."