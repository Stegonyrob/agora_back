# 🛡️ SISTEMA DE MODERACIÓN Y CENSURA - ITINERARIO DE IMPLEMENTACIÓN

> **Fecha de Creación:** 26 de Agosto, 2025  
> **Estado Actual:** Censura de comentarios implementada básicamente  
> **Próxima Sesión:** Continuar con implementación completa

---

## 📊 **ESTADO ACTUAL DEL SISTEMA**

### ✅ **Implementado**
- [x] **Censura básica de comentarios** - `CommentServiceImpl`
- [x] **Lista de palabras ofensivas** - `ModerationServiceImpl`
- [x] **Integración con análisis de sentimientos** - `SentimentAnalysisService`
- [x] **Rechazo automático** - Lanza `IllegalArgumentException`

### ⚠️ **Pendiente de Verificación**
- [ ] **Pruebas funcionales** del sistema de censura
- [ ] **Censura de replies** - Falta implementar en `ReplyServiceImpl`
- [ ] **Notificaciones Kafka** para administradores

### ❌ **No Implementado**
- [ ] Sistema de conteo de faltas
- [ ] Sistema de baneo/suspensión
- [ ] Panel de administración de moderación
- [ ] Tests automatizados

---

## 🗓️ **ITINERARIO COMPLETO DE IMPLEMENTACIÓN**

## **FASE 1: VERIFICACIÓN Y EXTENSIÓN DEL SISTEMA DE CENSURA**
*Tiempo estimado: 15-20 minutos*

### 🎯 **Objetivo:** Asegurar que la censura funciona en comentarios y replies

#### **1.1 Verificar Censura de Comentarios** *(5 min)*
```bash
# Pasos de prueba:
1. Enviar comentario con "estúpido" → Debe ser rechazado
2. Enviar comentario con "tonto" → Debe ser rechazado  
3. Enviar comentario normal → Debe ser aceptado
4. Verificar que se guarda en CensuredComment
```

#### **1.2 Implementar Censura en Replies** *(10-15 min)*
**Archivo:** `src/main/java/de/stella/agora_web/replies/service/impl/ReplyServiceImpl.java`

```java
// Agregar import
import de.stella.agora_web.moderation.service.IModerationService;

// Inyectar servicio
@Autowired
private IModerationService moderationService;

// En createReply(), antes de guardar:
// ✅ MODERACIÓN: Verificar contenido inapropiado
var censuredReply = moderationService.moderateComment(reply);
if (censuredReply != null) {
    throw new IllegalArgumentException("Respuesta rechazada por contenido inapropiado");
}
```

---

## **FASE 2: SISTEMA DE CONTEO DE FALTAS Y SANCIONES**
*Tiempo estimado: 25-30 minutos*

### 🎯 **Objetivo:** Implementar sistema de sanciones progresivas

#### **2.1 Crear Entidad UserViolation** *(8-10 min)*
**Archivo:** `src/main/java/de/stella/agora_web/violations/model/UserViolation.java`

```java
@Entity
@Table(name = "user_violations")
@Getter @Setter
public class UserViolation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    
    @Enumerated(EnumType.STRING)
    private ViolationType type; // OFFENSIVE_COMMENT, SPAM, HARASSMENT
    
    private String content; // Contenido que causó la violación
    private String reason;  // Razón específica de la violación
    
    @Column(name = "violation_date")
    private LocalDateTime violationDate;
    
    @Enumerated(EnumType.STRING)
    private ViolationStatus status; // ACTIVE, RESOLVED, APPEALED
    
    private Long relatedContentId; // ID del comentario/reply
    private String relatedContentType; // "COMMENT" o "REPLY"
}
```

#### **2.2 Crear ViolationService** *(10-12 min)*
**Archivo:** `src/main/java/de/stella/agora_web/violations/service/ViolationService.java`

```java
@Service
public class ViolationService {
    
    // Registrar nueva violación
    public void recordViolation(User user, String content, ViolationType type);
    
    // Contar violaciones activas del usuario
    public int countActiveViolations(User user);
    
    // Determinar si requiere sanción
    public SanctionLevel determineSanctionLevel(User user);
    
    // Aplicar sanción automática
    public void applySanction(User user, SanctionLevel level);
}
```

#### **2.3 Crear Sistema de Sanciones** *(7-8 min)*
**Archivo:** `src/main/java/de/stella/agora_web/sanctions/service/SanctionService.java`

```java
// Niveles de sanción
public enum SanctionLevel {
    WARNING(0),           // 1-2 faltas: Solo advertencia
    TEMPORARY_BAN(7),     // 3-4 faltas: 7 días de suspensión
    EXTENDED_BAN(30),     // 5-6 faltas: 30 días de suspensión  
    PERMANENT_BAN(-1);    // 7+ faltas: Baneo permanente
}
```

---

## **FASE 3: NOTIFICACIONES KAFKA PARA ADMINISTRADORES**
*Tiempo estimado: 20-25 minutos*

### 🎯 **Objetivo:** Alertar a administradores en tiempo real

#### **3.1 Configurar Topics Kafka** *(5 min)*
**Archivo:** `src/main/resources/application.yml`

```yaml
spring:
  kafka:
    producer:
      bootstrap-servers: localhost:9092
    consumer:
      bootstrap-servers: localhost:9092
      group-id: agora-moderation
      
agora:
  kafka:
    topics:
      moderation: "comment-moderation"
      violations: "user-violations" 
      admin-alerts: "admin-alerts"
```

#### **3.2 Crear ModerationKafkaProducer** *(8-10 min)*
**Archivo:** `src/main/java/de/stella/agora_web/moderation/kafka/ModerationKafkaProducer.java`

```java
@Component
public class ModerationKafkaProducer {
    
    // Notificar contenido censurado
    public void sendContentCensored(ContentCensoredDTO dto);
    
    // Notificar nueva violación
    public void sendViolationAlert(ViolationAlertDTO dto);
    
    // Notificar sanción aplicada
    public void sendSanctionAlert(SanctionAlertDTO dto);
}
```

#### **3.3 Integrar en ModerationService** *(7-10 min)*
```java
// En ModerationServiceImpl.moderateComment()
if (censuredComment != null) {
    // Registrar violación
    violationService.recordViolation(comment.getUser(), 
        comment.getMessage(), ViolationType.OFFENSIVE_COMMENT);
    
    // Notificar a admins
    kafkaProducer.sendContentCensored(new ContentCensoredDTO(
        comment.getUser().getUsername(),
        comment.getMessage(),
        "Contenido ofensivo detectado"
    ));
    
    return censuredComment;
}
```

---

## **FASE 4: PANEL DE ADMINISTRACIÓN**
*Tiempo estimado: 20-25 minutos*

### 🎯 **Objetivo:** Dashboard para gestionar moderación

#### **4.1 ModerationController** *(12-15 min)*
**Endpoints necesarios:**
```java
@RestController
@RequestMapping("/api/v1/admin/moderation")
public class ModerationController {
    
    @GetMapping("/violations")
    public Page<UserViolation> getPendingViolations(Pageable pageable);
    
    @GetMapping("/violations/user/{userId}")
    public List<UserViolation> getUserViolations(@PathVariable Long userId);
    
    @PostMapping("/sanctions/{userId}")
    public ResponseEntity<?> applySanction(@PathVariable Long userId, 
                                         @RequestBody SanctionRequest request);
    
    @GetMapping("/censored-content")
    public Page<CensuredComment> getCensuredContent(Pageable pageable);
    
    @PutMapping("/violations/{violationId}/resolve")
    public ResponseEntity<?> resolveViolation(@PathVariable Long violationId);
}
```

#### **4.2 Dashboard Frontend** *(8-10 min)*
- Vista de violaciones pendientes
- Historial de sanciones por usuario
- Estadísticas de moderación
- Herramientas de gestión de baneos

---

## **FASE 5: TESTING Y VALIDACIÓN**
*Tiempo estimado: 15-20 minutos*

### 🎯 **Objetivo:** Asegurar funcionamiento correcto

#### **5.1 Tests Unitarios** *(8-10 min)*
```java
@SpringBootTest
class ModerationSystemTest {
    
    @Test
    void shouldCensorOffensiveComment();
    
    @Test  
    void shouldRecordViolationAfterCensorship();
    
    @Test
    void shouldApplySanctionAfterMultipleViolations();
    
    @Test
    void shouldSendKafkaNotificationToAdmins();
}
```

#### **5.2 Pruebas de Integración** *(7-10 min)*
```bash
# Escenario completo:
1. Usuario envía comentario ofensivo → Censurado + Violación registrada
2. Usuario reincide 3 veces → Suspensión temporal aplicada
3. Admin recibe notificaciones Kafka → Verificar en dashboard
4. Usuario baneado intenta comentar → Rechazado
```

---

## 🔧 **OPTIMIZACIONES Y REFACTORIZACIONES**

### **Mejoras de Performance**
- [ ] **Cache de palabras ofensivas** - Redis/In-memory
- [ ] **Procesamiento asíncrono** - @Async para moderación
- [ ] **Batch processing** - Procesar violaciones en lotes

### **Mejoras de Detección**
- [ ] **Machine Learning** - Integrar modelo avanzado de detección
- [ ] **Contexto semántico** - Análisis más sofisticado
- [ ] **Evasión de filtros** - Detectar l33t speak, espacios, etc.

### **Mejoras de Gestión**
- [ ] **Appeals system** - Sistema de apelaciones
- [ ] **Moderador humano** - Workflow para revisión manual
- [ ] **Escalado automático** - Políticas de sanción adaptativas

---

## 📝 **NOTAS IMPORTANTES**

### **Archivos Clave Modificados:**
- ✅ `CommentServiceImpl.java` - Moderación implementada
- ✅ `ModerationServiceImpl.java` - Lista de palabras ofensivas
- ⏳ `ReplyServiceImpl.java` - Pendiente de moderación

### **Configuración Necesaria:**
- Kafka topics configurados
- Base de datos con tablas de violaciones
- Roles de administrador configurados

### **Puntos de Atención:**
- **Performance:** La moderación añade latencia a creación de contenido
- **False Positives:** Revisar regularmente palabras censuradas incorrectamente  
- **Escalabilidad:** Considerar volumen de contenido vs capacidad de moderación

---

## 🚀 **COMANDOS ÚTILES PARA MAÑANA**

```bash
# Compilar y ejecutar
mvn clean compile
mvn spring-boot:run

# Verificar Kafka topics
kafka-topics.sh --list --bootstrap-server localhost:9092

# Monitorear logs de moderación
tail -f logs/spring.log | grep -i "moderation\|censur"

# Verificar base de datos
mysql -u root -p -e "SELECT * FROM user_violations ORDER BY violation_date DESC LIMIT 10;"
```

---

**📅 PRÓXIMA SESIÓN:** Comenzar con FASE 1.2 - Implementar moderación en replies  
**⏱️ TIEMPO TOTAL ESTIMADO:** 95-120 minutos distribuidos en 5 fases

---

*¡Listo para continuar mañana con un sistema de moderación robusto y completo!* 🛡️
