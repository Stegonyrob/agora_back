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

## **FASE 6: CONFIGURACIÓN DE LOGIN SOCIAL (FACEBOOK Y GOOGLE)**
*Tiempo estimado: 30-35 minutos*

### 🎯 **Objetivo:** Implementar autenticación con redes sociales

#### **6.1 Configuración OAuth2 en application.yml** *(8-10 min)*
**Archivo:** `src/main/resources/application.yml`

```yaml
spring:
  security:
    oauth2:
      client:
        registration:
          google:
            client-id: ${GOOGLE_CLIENT_ID}
            client-secret: ${GOOGLE_CLIENT_SECRET}
            scope:
              - email
              - profile
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            
          facebook:
            client-id: ${FACEBOOK_CLIENT_ID}
            client-secret: ${FACEBOOK_CLIENT_SECRET}
            scope:
              - email
              - public_profile
            redirect-uri: "{baseUrl}/login/oauth2/code/{registrationId}"
            
        provider:
          google:
            authorization-uri: https://accounts.google.com/o/oauth2/v2/auth
            token-uri: https://oauth2.googleapis.com/token
            user-info-uri: https://www.googleapis.com/oauth2/v3/userinfo
            user-name-attribute: sub
            
          facebook:
            authorization-uri: https://www.facebook.com/v18.0/dialog/oauth
            token-uri: https://graph.facebook.com/v18.0/oauth/access_token
            user-info-uri: https://graph.facebook.com/v18.0/me?fields=id,name,email,picture
            user-name-attribute: id
```

#### **6.2 Configurar SecurityConfig** *(8-10 min)*
**Archivo:** `src/main/java/de/stella/agora_web/security/SecurityConfig.java`

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private OAuth2UserService<OAuth2UserRequest, OAuth2User> oauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                .requestMatchers("/login", "/oauth2/**", "/login/oauth2/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oauth2UserService)
                )
                .successHandler(new OAuth2AuthenticationSuccessHandler())
                .failureHandler(new OAuth2AuthenticationFailureHandler())
            )
            .jwt(jwt -> jwt.jwtDecoder(jwtDecoder()));
            
        return http.build();
    }
}
```

#### **6.3 Implementar CustomOAuth2UserService** *(10-12 min)*
**Archivo:** `src/main/java/de/stella/agora_web/auth/service/CustomOAuth2UserService.java`

```java
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oauth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return processOAuth2User(userRequest, oauth2User, registrationId);
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, 
                                       OAuth2User oauth2User, 
                                       String registrationId) {
        
        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oauth2User.getAttributes());
        
        if (userInfo.getEmail() == null || userInfo.getEmail().isEmpty()) {
            throw new OAuth2AuthenticationException("Email not found from OAuth2 provider");
        }

        Optional<User> userOptional = userRepository.findByEmail(userInfo.getEmail());
        User user;
        
        if (userOptional.isPresent()) {
            user = userOptional.get();
            user = updateExistingUser(user, userInfo);
        } else {
            user = registerNewUser(userRequest, userInfo);
        }

        return new SecurityUser(user, oauth2User.getAttributes());
    }

    private User registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo userInfo) {
        User user = new User();
        user.setUsername(generateUniqueUsername(userInfo.getName()));
        user.setEmail(userInfo.getEmail());
        user.setPassword(""); // OAuth users don't need password
        user.setAcceptedRules(true);
        
        // Asignar rol USER por defecto
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("Role not found: ROLE_USER"));
        user.getRoles().add(userRole);
        
        user = userRepository.save(user);

        // Crear perfil asociado
        Profile profile = new Profile();
        profile.setId(user.getId()); // Misma ID que el usuario
        profile.setFirstName(userInfo.getName());
        profile.setEmail(userInfo.getEmail());
        profile.setUsername(user.getUsername());
        profile.setUser(user);
        
        profileRepository.save(profile);

        return user;
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo userInfo) {
        existingUser.setUsername(userInfo.getName());
        return userRepository.save(existingUser);
    }

    private String generateUniqueUsername(String name) {
        String baseUsername = name.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();
        String username = baseUsername;
        int counter = 1;
        
        while (userRepository.findByUsername(username).isPresent()) {
            username = baseUsername + counter;
            counter++;
        }
        
        return username;
    }
}
```

#### **6.4 Crear OAuth2UserInfo Factory** *(4-5 min)*
**Archivo:** `src/main/java/de/stella/agora_web/auth/oauth2/OAuth2UserInfoFactory.java`

```java
public class OAuth2UserInfoFactory {
    
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        switch (registrationId.toLowerCase()) {
            case "google":
                return new GoogleOAuth2UserInfo(attributes);
            case "facebook":
                return new FacebookOAuth2UserInfo(attributes);
            default:
                throw new OAuth2AuthenticationException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}

// Interfaces y implementaciones
public abstract class OAuth2UserInfo {
    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();
}

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {
    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) attributes.get("picture");
    }
}

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {
    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        Map<String, Object> picture = (Map<String, Object>) attributes.get("picture");
        if (picture != null) {
            Map<String, Object> data = (Map<String, Object>) picture.get("data");
            if (data != null) {
                return (String) data.get("url");
            }
        }
        return null;
    }
}
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
- **🆕 Credenciales OAuth2:**
  - **Google Cloud Console:** Crear proyecto y obtener CLIENT_ID/CLIENT_SECRET
  - **Facebook Developers:** Crear app y configurar Login con Facebook
  - **Variables de entorno:** GOOGLE_CLIENT_ID, GOOGLE_CLIENT_SECRET, FACEBOOK_CLIENT_ID, FACEBOOK_CLIENT_SECRET

### **🆕 Pasos para Configurar OAuth2:**
```bash
# 1. Google Cloud Console
# - Ir a https://console.cloud.google.com/
# - Crear nuevo proyecto o usar existente
# - Habilitar Google+ API
# - Crear credenciales OAuth 2.0
# - Agregar redirect URI: http://localhost:8080/login/oauth2/code/google

# 2. Facebook Developers
# - Ir a https://developers.facebook.com/
# - Crear nueva app
# - Configurar Facebook Login
# - Agregar redirect URI: http://localhost:8080/login/oauth2/code/facebook

# 3. Variables de entorno (application.yml o .env)
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
FACEBOOK_CLIENT_ID=your-facebook-app-id
FACEBOOK_CLIENT_SECRET=your-facebook-app-secret
```

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
**⏱️ TIEMPO TOTAL ESTIMADO:** 125-155 minutos distribuidos en 6 fases
- **Moderación:** 95-120 minutos (Fases 1-5)
- **🆕 Login Social:** 30-35 minutos (Fase 6)

---

*¡Listo para continuar mañana con un sistema de moderación robusto y completo!* 🛡️
