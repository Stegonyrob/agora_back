# 🔐 GUÍA DE SEGURIDAD - VARIABLES DE ENTORNO

## ✅ Configuración Actual de Seguridad

### 📧 Email del Administrador
- **✅ BUENA PRÁCTICA**: Usado `@Value("${admin.email:admin@gmail.com}")` 
- **✅ BUENA PRÁCTICA**: Variable de entorno `ADMIN_EMAIL` para producción
- **✅ BUENA PRÁCTICA**: Valor por defecto para desarrollo local
- **✅ BUENA PRÁCTICA**: Documentado en `.env.example`

### 🔐 Variables Sensibles Protegidas
- **✅ `.env` incluido en `.gitignore`**
- **✅ Claves JWT privadas/públicas no incluidas en git**
- **✅ Credenciales de Google Cloud no incluidas en git**
- **✅ Cliente secret no incluido en git**

## 🚀 Configuración por Entorno

### 🏠 Desarrollo Local
```properties
# application-dev.properties
admin.email=admin.dev@gmail.com
```

### 🧪 Tests
```properties
# application-test.properties  
admin.email=admin.test@gmail.com
```

### 🌍 Producción
```bash
# Variables de entorno en servidor
export ADMIN_EMAIL=admin@tudominio.com
export RECAPTCHA_SECRET=tu_secret_real
export DB_PASSWORD=password_seguro
```

## 🐋 Configuración Docker

### Dockerfile
```dockerfile
# No incluir secrets en la imagen
ENV ADMIN_EMAIL=placeholder
```

### Docker Compose
```yaml
services:
  agora-backend:
    environment:
      - ADMIN_EMAIL=${ADMIN_EMAIL}
      - RECAPTCHA_SECRET=${RECAPTCHA_SECRET}
    env_file:
      - .env.production
```

### Docker Run
```bash
# Usar variables de entorno
docker run -e ADMIN_EMAIL=admin@tudominio.com agora-backend

# O usar archivo de entorno
docker run --env-file .env.production agora-backend
```

## ☸️ Configuración Kubernetes

### ConfigMap (datos no sensibles)
```yaml
apiVersion: v1
kind: ConfigMap
metadata:
  name: agora-config
data:
  ADMIN_EMAIL: "admin@tudominio.com"
```

### Secret (datos sensibles)
```yaml
apiVersion: v1
kind: Secret
metadata:
  name: agora-secrets
type: Opaque
data:
  RECAPTCHA_SECRET: <base64-encoded-secret>
  DB_PASSWORD: <base64-encoded-password>
```

### Deployment
```yaml
apiVersion: apps/v1
kind: Deployment
spec:
  template:
    spec:
      containers:
      - name: agora-backend
        envFrom:
        - configMapRef:
            name: agora-config
        - secretRef:
            name: agora-secrets
```

## 🔍 Verificación de Seguridad

### ✅ Checklist de Seguridad
- [ ] Variables sensibles no hardcodeadas en código
- [ ] `.env` incluido en `.gitignore`
- [ ] Valores por defecto seguros para desarrollo
- [ ] Documentación de variables en `.env.example`
- [ ] Diferentes configuraciones por entorno
- [ ] Secrets separados de configuración en K8s
- [ ] Variables de entorno configuradas en CI/CD

### 🚨 Señales de Alerta
- ❌ Emails hardcodeados en código fuente
- ❌ Passwords en archivos de configuración
- ❌ Secrets subidos a git
- ❌ Misma configuración en todos los entornos
- ❌ Variables sensibles en logs

## 📝 Logs de Seguridad

### ✅ Logs Seguros (NO muestran datos sensibles)
```java
logger.info("📧 Enviando notificación a admin");
logger.info("📄 Post: {}", notification.getPostTitle());
logger.info("👤 Usuario: {}", notification.getUserName());
```

### ❌ Logs Inseguros (EVITAR)
```java
// ❌ NO HACER ESTO
logger.info("Enviando email a: {}", adminEmail);
logger.info("Secret key: {}", secretKey);
logger.info("Password: {}", password);
```

## 🔧 Implementación Actual

### EmailNotificationService.java
```java
@Service
public class EmailNotificationService {
    
    // ✅ CORRECTO: Variable de entorno con valor por defecto
    @Value("${admin.email:admin@gmail.com}")
    private String adminEmail;
    
    public void sendCommentNotification(CommentNotificationDTO notification) {
        // ✅ CORRECTO: Log sin revelar email admin
        logger.info("📧 Enviando notificación de comentario a admin");
        
        // ✅ CORRECTO: Email usado internamente, no loggeado
        String emailContent = buildCommentEmailContent(notification);
        sendEmailToAdmin(emailContent);
    }
}
```

## 🔄 Rotación de Secrets

### Proceso Recomendado
1. **Generar nuevo secret**
2. **Actualizar en sistemas de gestión de secrets (K8s, Docker Swarm, etc.)**
3. **Desplegar nueva versión**
4. **Verificar funcionamiento**
5. **Invalidar secret anterior**

### Frecuencia
- **Claves JWT**: Cada 6 meses
- **reCAPTCHA**: Solo si comprometido
- **Passwords BD**: Cada 3 meses
- **Admin Email**: Solo si necesario

## 📚 Referencias
- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)
- [Docker Environment Variables](https://docs.docker.com/compose/environment-variables/)
- [Kubernetes Secrets](https://kubernetes.io/docs/concepts/configuration/secret/)
- [12 Factor App - Config](https://12factor.net/config)