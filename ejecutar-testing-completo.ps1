# 🧪 SCRIPT DE TESTING COMPLETO
# Para ejecutar el lunes - Verificación de testing y seguridad

Write-Host "🧪 INICIANDO TESTING COMPLETO - AGORA BACKEND" -ForegroundColor Cyan
Write-Host "================================================" -ForegroundColor Cyan

$START_TIME = Get-Date
$TOTAL_TESTS = 0
$PASSED_TESTS = 0
$FAILED_TESTS = 0
$SECURITY_CHECKS = 0

# 1. TESTS UNITARIOS
Write-Host ""
Write-Host "🔬 1. EJECUTANDO TESTS UNITARIOS..." -ForegroundColor Yellow
Write-Host "======================================"

try {
    # Ejecutar tests Maven
    Write-Host "Ejecutando: mvn test" -ForegroundColor Gray
    $testResult = & mvn test 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Tests unitarios - PASARON" -ForegroundColor Green
        
        # Extraer estadísticas de los tests
        $testOutput = $testResult -join "`n"
        if ($testOutput -match "Tests run: (\d+), Failures: (\d+), Errors: (\d+), Skipped: (\d+)") {
            $TOTAL_TESTS = [int]$matches[1]
            $failures = [int]$matches[2]
            $errors = [int]$matches[3]
            $skipped = [int]$matches[4]
            $PASSED_TESTS = $TOTAL_TESTS - $failures - $errors - $skipped
            $FAILED_TESTS = $failures + $errors
            
            Write-Host "   📊 Total: $TOTAL_TESTS | ✅ Pasaron: $PASSED_TESTS | ❌ Fallaron: $FAILED_TESTS | ⏭️ Omitidos: $skipped"
        }
    } else {
        Write-Host "❌ Tests unitarios - FALLARON" -ForegroundColor Red
        Write-Host "Revisa los logs para más detalles" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error ejecutando tests: $($_.Exception.Message)" -ForegroundColor Red
}

# 2. VERIFICACIÓN DE COBERTURA
Write-Host ""
Write-Host "📊 2. VERIFICANDO COBERTURA DE CÓDIGO..." -ForegroundColor Yellow
Write-Host "========================================"

# Buscar archivos de test
$testFiles = Get-ChildItem -Recurse -Path "src\test\java" -Filter "*.java" -ErrorAction SilentlyContinue
$srcFiles = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*.java" -ErrorAction SilentlyContinue

if ($testFiles -and $srcFiles) {
    $coverageRatio = [math]::Round(($testFiles.Count / $srcFiles.Count) * 100, 2)
    Write-Host "📈 Ratio Test/Source: $coverageRatio% ($($testFiles.Count) tests / $($srcFiles.Count) archivos)" 
    
    if ($coverageRatio -ge 80) {
        Write-Host "✅ Cobertura excelente (≥80%)" -ForegroundColor Green
    } elseif ($coverageRatio -ge 60) {
        Write-Host "⚠️  Cobertura buena (60-79%)" -ForegroundColor Yellow
    } else {
        Write-Host "❌ Cobertura baja (<60%) - Agregar más tests" -ForegroundColor Red
    }
}

# 3. TESTING DE SEGURIDAD
Write-Host ""
Write-Host "🔒 3. VERIFICACIONES DE SEGURIDAD..." -ForegroundColor Yellow
Write-Host "====================================="

# 3.1 Verificar endpoints protegidos
Write-Host "🔍 3.1 Verificando endpoints protegidos..."
$controllers = Get-ChildItem -Recurse -Path "src\main\java" -Filter "*Controller.java" -ErrorAction SilentlyContinue

$protectedEndpoints = 0
$unprotectedEndpoints = 0

foreach ($controller in $controllers) {
    $content = Get-Content $controller.FullName -Raw
    
    # Buscar métodos HTTP
    $httpMethods = [regex]::Matches($content, '@(Get|Post|Put|Delete|Patch)Mapping')
    
    foreach ($method in $httpMethods) {
        $SECURITY_CHECKS++
        
        # Buscar @PreAuthorize en la misma línea o líneas cercanas
        $methodLine = $content.Substring(0, $method.Index).Split("`n").Count
        $methodContext = ($content -split "`n")[$methodLine-3..$methodLine+3] -join "`n"
        
        if ($methodContext -match "@PreAuthorize|@Secured|@RolesAllowed") {
            $protectedEndpoints++
        } else {
            $unprotectedEndpoints++
            Write-Host "   ⚠️  Endpoint posiblemente no protegido en $($controller.Name)" -ForegroundColor Yellow
        }
    }
}

Write-Host "   📊 Endpoints protegidos: $protectedEndpoints | No protegidos: $unprotectedEndpoints"

# 3.2 Verificar validaciones de entrada
Write-Host "🔍 3.2 Verificando validaciones de entrada..."
$validationCount = 0

foreach ($controller in $controllers) {
    $content = Get-Content $controller.FullName -Raw
    $validations = [regex]::Matches($content, '@Valid|@Validated|@NotNull|@NotEmpty|@Size').Count
    $validationCount += $validations
}

Write-Host "   📊 Anotaciones de validación encontradas: $validationCount"

if ($validationCount -gt 0) {
    Write-Host "   ✅ Validaciones de entrada - OK" -ForegroundColor Green
} else {
    Write-Host "   ⚠️  Pocas validaciones - Considerar agregar más" -ForegroundColor Yellow
}

# 3.3 Verificar configuración JWT
Write-Host "🔍 3.3 Verificando configuración JWT..."
$securityConfig = "src\main\java\de\stella\agora_web\config\SecurityConfiguration.java"

if (Test-Path $securityConfig) {
    $content = Get-Content $securityConfig -Raw
    
    $jwtFeatures = @(
        @{Name="JwtDecoder"; Pattern="JwtDecoder"},
        @{Name="JwtEncoder"; Pattern="JwtEncoder"},
        @{Name="JWT Authentication"; Pattern="jwtAuthenticationConverter"},
        @{Name="OAuth2 Resource Server"; Pattern="oauth2ResourceServer"}
    )
    
    foreach ($feature in $jwtFeatures) {
        if ($content -match $feature.Pattern) {
            Write-Host "   ✅ $($feature.Name) - Configurado" -ForegroundColor Green
        } else {
            Write-Host "   ❌ $($feature.Name) - No encontrado" -ForegroundColor Red
        }
    }
}

# 4. TESTS DE INTEGRACIÓN
Write-Host ""
Write-Host "🔗 4. VERIFICANDO TESTS DE INTEGRACIÓN..." -ForegroundColor Yellow
Write-Host "=========================================="

$integrationTests = Get-ChildItem -Recurse -Path "src\test\java" -Filter "*IntegrationTest.java" -ErrorAction SilentlyContinue
if ($integrationTests) {
    Write-Host "✅ Tests de integración encontrados: $($integrationTests.Count)" -ForegroundColor Green
    foreach ($test in $integrationTests) {
        Write-Host "   📋 $($test.Name)"
    }
} else {
    Write-Host "⚠️  No se encontraron tests de integración específicos" -ForegroundColor Yellow
    Write-Host "   💡 Considerar crear tests con @SpringBootTest" -ForegroundColor Cyan
}

# 5. VERIFICAR CONFIGURACIÓN DE PROFILES
Write-Host ""
Write-Host "⚙️  5. VERIFICANDO CONFIGURACIÓN DE PROFILES..." -ForegroundColor Yellow
Write-Host "==============================================="

$profiles = @("dev", "prod", "test")
foreach ($profile in $profiles) {
    $configFile = "src\main\resources\application-$profile.properties"
    if (Test-Path $configFile) {
        Write-Host "   ✅ application-$profile.properties - Existe" -ForegroundColor Green
    } else {
        Write-Host "   ⚠️  application-$profile.properties - No existe" -ForegroundColor Yellow
    }
}

# 6. VERIFICAR LOGS Y CONFIGURACIÓN DE MONITOREO
Write-Host ""
Write-Host "📊 6. VERIFICANDO CONFIGURACIÓN DE MONITOREO..." -ForegroundColor Yellow
Write-Host "==============================================="

$mainConfig = "src\main\resources\application.properties"
if (Test-Path $mainConfig) {
    $content = Get-Content $mainConfig -Raw
    
    $monitoringFeatures = @(
        @{Name="Logging Levels"; Pattern="logging\.level"},
        @{Name="Actuator"; Pattern="management\.endpoints"},
        @{Name="Health Checks"; Pattern="management\.health"},
        @{Name="JPA Show SQL"; Pattern="spring\.jpa\.show-sql"}
    )
    
    foreach ($feature in $monitoringFeatures) {
        if ($content -match $feature.Pattern) {
            Write-Host "   ✅ $($feature.Name) - Configurado" -ForegroundColor Green
        } else {
            Write-Host "   ⚠️  $($feature.Name) - No configurado" -ForegroundColor Yellow
        }
    }
}

# 7. COMPILACIÓN COMPLETA
Write-Host ""
Write-Host "🏗️  7. VERIFICANDO COMPILACIÓN COMPLETA..." -ForegroundColor Yellow
Write-Host "==========================================="

try {
    Write-Host "Ejecutando: mvn clean compile" -ForegroundColor Gray
    $compileResult = & mvn clean compile 2>&1
    
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilación completa - EXITOSA" -ForegroundColor Green
    } else {
        Write-Host "❌ Compilación completa - FALLÓ" -ForegroundColor Red
        Write-Host "Revisa los logs para más detalles" -ForegroundColor Yellow
    }
} catch {
    Write-Host "❌ Error en compilación: $($_.Exception.Message)" -ForegroundColor Red
}

# RESUMEN FINAL
$END_TIME = Get-Date
$DURATION = $END_TIME - $START_TIME

Write-Host ""
Write-Host "🎯 RESUMEN FINAL DEL TESTING" -ForegroundColor Cyan
Write-Host "=============================" -ForegroundColor Cyan
Write-Host "⏱️  Duración total: $($DURATION.TotalMinutes.ToString('0.00')) minutos"
Write-Host "🧪 Tests ejecutados: $TOTAL_TESTS"
Write-Host "✅ Tests pasaron: $PASSED_TESTS" -ForegroundColor Green
Write-Host "❌ Tests fallaron: $FAILED_TESTS" -ForegroundColor Red
Write-Host "🔒 Verificaciones de seguridad: $SECURITY_CHECKS"
Write-Host "🔐 Endpoints protegidos: $protectedEndpoints"
Write-Host "⚠️  Endpoints no protegidos: $unprotectedEndpoints" -ForegroundColor Yellow

# Generar reporte
$reportContent = @"
# 📊 REPORTE DE TESTING - $(Get-Date -Format 'yyyy-MM-dd HH:mm:ss')

## Resumen Ejecutivo
- **Duración total**: $($DURATION.TotalMinutes.ToString('0.00')) minutos
- **Tests ejecutados**: $TOTAL_TESTS
- **Tests exitosos**: $PASSED_TESTS
- **Tests fallidos**: $FAILED_TESTS
- **Verificaciones de seguridad**: $SECURITY_CHECKS

## Estado General
$(if ($FAILED_TESTS -eq 0) { "✅ **ESTADO: EXITOSO** - Todos los tests pasaron" } else { "❌ **ESTADO: REQUIERE ATENCIÓN** - $FAILED_TESTS tests fallaron" })

## Recomendaciones para Producción
1. $(if ($FAILED_TESTS -eq 0) { "✅ Tests unitarios OK" } else { "❌ Corregir $FAILED_TESTS tests fallidos" })
2. $(if ($unprotectedEndpoints -eq 0) { "✅ Seguridad de endpoints OK" } else { "⚠️ Revisar $unprotectedEndpoints endpoints no protegidos" })
3. $(if ($validationCount -gt 10) { "✅ Validaciones de entrada OK" } else { "⚠️ Considerar agregar más validaciones" })
4. $(if ($integrationTests) { "✅ Tests de integración presentes" } else { "⚠️ Agregar tests de integración" })

## Próximos Pasos
- [ ] Corregir tests fallidos
- [ ] Implementar tests faltantes  
- [ ] Revisar seguridad de endpoints
- [ ] Verificar configuraciones de producción
- [ ] Ejecutar tests de carga si es necesario

---
*Generado automáticamente por el script de testing*
"@

$reportContent | Out-File -FilePath "reporte-testing-$(Get-Date -Format 'yyyy-MM-dd').md" -Encoding UTF8

Write-Host ""
Write-Host "📄 Reporte generado: reporte-testing-$(Get-Date -Format 'yyyy-MM-dd').md" -ForegroundColor Cyan

Write-Host ""
if ($FAILED_TESTS -eq 0 -and $unprotectedEndpoints -lt 3) {
    Write-Host "🎉 ¡EXCELENTE! El backend está listo para producción." -ForegroundColor Green
} elseif ($FAILED_TESTS -eq 0) {
    Write-Host "✅ Tests OK, pero revisar seguridad de endpoints." -ForegroundColor Yellow
} else {
    Write-Host "🚨 REQUIERE ATENCIÓN: Hay tests fallidos que deben corregirse." -ForegroundColor Red
}

Write-Host ""
Write-Host "🏁 Testing completo finalizado. ¡Revisa el reporte generado!" -ForegroundColor Green
Read-Host "Presiona Enter para continuar..."