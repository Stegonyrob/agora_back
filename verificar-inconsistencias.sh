#!/bin/bash

# 🔍 SCRIPT DE VERIFICACIÓN DE INCONSISTENCIAS
# Para ejecutar el lunes antes de los cambios

echo "🚀 INICIANDO ESCANEO DE INCONSISTENCIAS - AGORA BACKEND"
echo "=================================================="

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Contadores
ERRORS=0
WARNINGS=0
CHECKS=0

echo -e "${YELLOW}📋 VERIFICANDO ESTRUCTURA DEL PROYECTO...${NC}"

# 1. Verificar archivos críticos
echo "🔍 1. Verificando archivos críticos..."
CRITICAL_FILES=(
    "src/main/java/de/stella/agora_web/config/SecurityConfiguration.java"
    "src/main/resources/application.properties"
    "src/main/resources/data.sql"
    "pom.xml"
)

for file in "${CRITICAL_FILES[@]}"; do
    ((CHECKS++))
    if [ -f "$file" ]; then
        echo -e "   ✅ $file - EXISTE"
    else
        echo -e "   ${RED}❌ $file - FALTA${NC}"
        ((ERRORS++))
    fi
done

# 2. Verificar consistencia en DTOs
echo "🔍 2. Verificando DTOs..."
((CHECKS++))
DTO_FILES=$(find src/main/java -name "*DTO.java" -type f)
if [ -n "$DTO_FILES" ]; then
    echo -e "   ✅ DTOs encontrados: $(echo "$DTO_FILES" | wc -l)"
    # Verificar que todos los DTOs tengan @Data o getters/setters
    for dto in $DTO_FILES; do
        if ! grep -q "@Data\|@Getter\|@Setter" "$dto"; then
            echo -e "   ${YELLOW}⚠️  $dto - Posible falta de anotaciones Lombok${NC}"
            ((WARNINGS++))
        fi
    done
else
    echo -e "   ${RED}❌ No se encontraron DTOs${NC}"
    ((ERRORS++))
fi

# 3. Verificar controladores
echo "🔍 3. Verificando controladores..."
((CHECKS++))
CONTROLLER_FILES=$(find src/main/java -name "*Controller.java" -type f)
if [ -n "$CONTROLLER_FILES" ]; then
    echo -e "   ✅ Controladores encontrados: $(echo "$CONTROLLER_FILES" | wc -l)"
    # Verificar anotaciones REST
    for controller in $CONTROLLER_FILES; do
        if ! grep -q "@RestController\|@Controller" "$controller"; then
            echo -e "   ${RED}❌ $controller - Falta anotación @RestController${NC}"
            ((ERRORS++))
        fi
        if ! grep -q "@RequestMapping" "$controller"; then
            echo -e "   ${YELLOW}⚠️  $controller - Posible falta de @RequestMapping${NC}"
            ((WARNINGS++))
        fi
    done
else
    echo -e "   ${RED}❌ No se encontraron controladores${NC}"
    ((ERRORS++))
fi

# 4. Verificar servicios e interfaces
echo "🔍 4. Verificando servicios..."
((CHECKS++))
SERVICE_FILES=$(find src/main/java -name "*Service.java" -type f)
INTERFACE_FILES=$(find src/main/java -name "I*Service.java" -type f)

echo -e "   📊 Servicios encontrados: $(echo "$SERVICE_FILES" | wc -l)"
echo -e "   📊 Interfaces encontradas: $(echo "$INTERFACE_FILES" | wc -l)"

# Verificar implementaciones de interfaces
for interface in $INTERFACE_FILES; do
    interface_name=$(basename "$interface" .java)
    impl_name="${interface_name#I}Impl"
    if ! find src/main/java -name "${impl_name}.java" -type f | grep -q .; then
        echo -e "   ${YELLOW}⚠️  $interface - No se encontró implementación ${impl_name}${NC}"
        ((WARNINGS++))
    fi
done

# 5. Verificar entidades JPA
echo "🔍 5. Verificando entidades JPA..."
((CHECKS++))
ENTITY_FILES=$(find src/main/java -name "*.java" -type f -exec grep -l "@Entity" {} \;)
if [ -n "$ENTITY_FILES" ]; then
    echo -e "   ✅ Entidades encontradas: $(echo "$ENTITY_FILES" | wc -l)"
    for entity in $ENTITY_FILES; do
        # Verificar anotaciones básicas JPA
        if ! grep -q "@Table\|@Id" "$entity"; then
            echo -e "   ${YELLOW}⚠️  $entity - Verificar anotaciones JPA${NC}"
            ((WARNINGS++))
        fi
    done
else
    echo -e "   ${RED}❌ No se encontraron entidades JPA${NC}"
    ((ERRORS++))
fi

# 6. Verificar configuración de seguridad
echo "🔍 6. Verificando configuración de seguridad..."
((CHECKS++))
SECURITY_CONFIG="src/main/java/de/stella/agora_web/config/SecurityConfiguration.java"
if [ -f "$SECURITY_CONFIG" ]; then
    if grep -q "@EnableWebSecurity\|@Configuration" "$SECURITY_CONFIG"; then
        echo -e "   ✅ Configuración de seguridad - OK"
    else
        echo -e "   ${RED}❌ Configuración de seguridad - Faltan anotaciones${NC}"
        ((ERRORS++))
    fi
    
    # Verificar JWT configuration
    if grep -q "jwtAuthenticationConverter\|JwtDecoder" "$SECURITY_CONFIG"; then
        echo -e "   ✅ Configuración JWT - OK"
    else
        echo -e "   ${YELLOW}⚠️  Configuración JWT - Verificar implementación${NC}"
        ((WARNINGS++))
    fi
else
    echo -e "   ${RED}❌ Archivo de configuración de seguridad no encontrado${NC}"
    ((ERRORS++))
fi

# 7. Verificar tests
echo "🔍 7. Verificando tests..."
((CHECKS++))
TEST_FILES=$(find src/test/java -name "*Test.java" -type f 2>/dev/null)
if [ -n "$TEST_FILES" ]; then
    TEST_COUNT=$(echo "$TEST_FILES" | wc -l)
    echo -e "   ✅ Tests encontrados: $TEST_COUNT"
    if [ "$TEST_COUNT" -lt 5 ]; then
        echo -e "   ${YELLOW}⚠️  Pocos tests - Considerar agregar más${NC}"
        ((WARNINGS++))
    fi
else
    echo -e "   ${YELLOW}⚠️  No se encontraron tests${NC}"
    ((WARNINGS++))
fi

# 8. Verificar dependencias críticas en pom.xml
echo "🔍 8. Verificando dependencias en pom.xml..."
((CHECKS++))
if [ -f "pom.xml" ]; then
    CRITICAL_DEPS=(
        "spring-boot-starter-web"
        "spring-boot-starter-security"
        "spring-boot-starter-data-jpa"
        "spring-boot-starter-oauth2-resource-server"
    )
    
    for dep in "${CRITICAL_DEPS[@]}"; do
        if grep -q "$dep" pom.xml; then
            echo -e "   ✅ $dep - OK"
        else
            echo -e "   ${RED}❌ $dep - FALTA${NC}"
            ((ERRORS++))
        fi
    done
else
    echo -e "   ${RED}❌ pom.xml no encontrado${NC}"
    ((ERRORS++))
fi

# 9. Verificar archivos de configuración
echo "🔍 9. Verificando archivos de configuración..."
((CHECKS++))
CONFIG_FILES=(
    "src/main/resources/application.properties"
    "src/main/resources/application-dev.properties"
    "src/main/resources/application-prod.properties"
)

for config in "${CONFIG_FILES[@]}"; do
    if [ -f "$config" ]; then
        echo -e "   ✅ $config - EXISTE"
        
        # Verificar configuraciones críticas
        if [ "$config" == "src/main/resources/application.properties" ]; then
            if ! grep -q "spring.jpa.hibernate.ddl-auto" "$config"; then
                echo -e "   ${YELLOW}⚠️  Falta configuración DDL en $config${NC}"
                ((WARNINGS++))
            fi
        fi
    else
        echo -e "   ${YELLOW}⚠️  $config - NO EXISTE${NC}"
        ((WARNINGS++))
    fi
done

# 10. Verificar estructura de directorios
echo "🔍 10. Verificando estructura de directorios..."
((CHECKS++))
REQUIRED_DIRS=(
    "src/main/java/de/stella/agora_web"
    "src/main/resources"
    "src/test/java"
    "target"
)

for dir in "${REQUIRED_DIRS[@]}"; do
    if [ -d "$dir" ]; then
        echo -e "   ✅ $dir - OK"
    else
        echo -e "   ${YELLOW}⚠️  $dir - NO EXISTE${NC}"
        ((WARNINGS++))
    fi
done

# RESUMEN FINAL
echo ""
echo "🎯 RESUMEN DEL ESCANEO"
echo "====================="
echo -e "📋 Verificaciones realizadas: $CHECKS"
echo -e "✅ Sin problemas: $((CHECKS - ERRORS - WARNINGS))"
echo -e "${YELLOW}⚠️  Advertencias: $WARNINGS${NC}"
echo -e "${RED}❌ Errores críticos: $ERRORS${NC}"

echo ""
if [ $ERRORS -eq 0 ] && [ $WARNINGS -eq 0 ]; then
    echo -e "${GREEN}🎉 ¡TODO PERFECTO! El proyecto está listo para el siguiente paso.${NC}"
elif [ $ERRORS -eq 0 ]; then
    echo -e "${YELLOW}⚠️  Hay algunas advertencias, pero el proyecto está mayormente bien.${NC}"
else
    echo -e "${RED}🚨 HAY ERRORES CRÍTICOS que deben ser corregidos antes de continuar.${NC}"
fi

echo ""
echo "📝 PRÓXIMOS PASOS PARA EL LUNES:"
echo "1. Corregir errores críticos encontrados"
echo "2. Revisar advertencias y decidir si requieren acción"
echo "3. Ejecutar tests completos"
echo "4. Cambiar configuración de BD a persistente"
echo "5. Verificar persistencia de datos"

echo ""
echo "🏁 Escaneo completado. ¡Revisa los resultados y prepárate para el lunes!"