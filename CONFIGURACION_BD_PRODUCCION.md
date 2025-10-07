# 🔧 CONFIGURACIONES PARA PRODUCCIÓN

## 📋 CHECKLIST MODIFICACIONES BD

### ✅ CAMBIOS REALIZADOS:
- [x] Creado respaldo de data.sql
- [ ] Modificar configuración DDL
- [ ] Crear configuraciones por entorno
- [ ] Tests de persistencia

---

## 🗄️ CONFIGURACIÓN BASE DE DATOS

### CONFIGURACIÓN ACTUAL (development):
```properties
# Para desarrollo - recrea BD cada vez
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
```

### CONFIGURACIÓN NUEVA (persistente):
```properties
# Para producción/testing - mantiene datos
spring.jpa.hibernate.ddl-auto=update
spring.sql.init.mode=never
```

---

## 📁 ARCHIVOS DE CONFIGURACIÓN

### application-dev.properties (desarrollo):
```properties
# Desarrollo - datos temporales
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.jpa.defer-datasource-initialization=true
```

### application-prod.properties (producción):
```properties
# Producción - datos persistentes
spring.jpa.hibernate.ddl-auto=validate
spring.sql.init.mode=never
spring.jpa.show-sql=false
```

### application-test.properties (testing):
```properties
# Testing - BD en memoria con datos de prueba
spring.jpa.hibernate.ddl-auto=create-drop
spring.sql.init.mode=always
spring.datasource.url=jdbc:h2:mem:testdb
```

---

## 🔄 ESTRATEGIAS DE INICIALIZACIÓN

### 1. PRIMERA VEZ (Inicialización):
- Usar `ddl-auto=create` + `data.sql`
- Cargar datos iniciales
- Cambiar a `ddl-auto=update`

### 2. DESARROLLO CONTINUO:
- Usar `ddl-auto=update`
- Sin `data.sql` automático
- Migrar cambios de esquema

### 3. PRODUCCIÓN:
- Usar `ddl-auto=validate`
- Solo validar esquema existente
- Migrar con scripts SQL

---

## 📊 SCRIPTS DE MIGRACIÓN

### migration-v1.sql (ejemplo):
```sql
-- Agregar columna archived a tabla posts
ALTER TABLE posts ADD COLUMN archived BOOLEAN DEFAULT false;

-- Actualizar registros existentes
UPDATE posts SET archived = false WHERE archived IS NULL;
```

### rollback-v1.sql (ejemplo):
```sql
-- Revertir cambios si es necesario
ALTER TABLE posts DROP COLUMN archived;
```

---

## 🧪 TESTS DE PERSISTENCIA

### Test 1: Reinicio del Servidor
1. Crear datos de prueba
2. Reiniciar aplicación
3. Verificar datos existen
4. Verificar integridad referencial

### Test 2: Modificación de Datos
1. Crear usuario de prueba
2. Modificar perfil
3. Reiniciar servidor
4. Verificar cambios persisten

### Test 3: Operaciones CRUD
1. Create: Insertar nuevos registros
2. Read: Consultar datos existentes
3. Update: Modificar registros
4. Delete: Eliminar (soft delete)

---

## 🚨 PLAN DE CONTINGENCIA

### Si falla la migración:
1. Parar aplicación
2. Restaurar BD desde backup
3. Revertir configuración
4. Analizar logs de error
5. Aplicar fix y reintentar

### Comandos de restauración:
```bash
# Restaurar desde backup
mysql -u user -p database < data-backup-2025-XX-XX.sql

# O para H2
# Copiar archivo de BD desde backup
```

---

## 📈 MONITOREO POST-CAMBIO

### Métricas a monitorear:
- [ ] Tiempo de inicio de aplicación
- [ ] Rendimiento de consultas
- [ ] Uso de memoria
- [ ] Errores en logs
- [ ] Integridad de datos

### Logs importantes:
```
org.hibernate.SQL=DEBUG  # Para ver queries
org.springframework.orm.jpa=DEBUG  # Para JPA
de.stella.agora_web=DEBUG  # Para app
```

---

## ✅ VERIFICACIONES FINALES

### Antes de producción:
- [ ] Todos los tests pasan
- [ ] No hay datos de prueba en producción
- [ ] Configuraciones correctas por entorno
- [ ] Backup automatizado configurado
- [ ] Monitoreo activo
- [ ] Documentación actualizada

---

**📅 Para el lunes: Ejecutar este plan paso a paso**