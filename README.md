# Agora Web - Educational Platform Backend

## 🎓 Overview

Agora Web is a comprehensive educational platform backend built with Java Spring Boot, featuring a robust content management system with real-time moderation, user-generated content, and advanced notification capabilities. The platform supports both preloaded and custom user avatars, dynamic legal text management, and a complete Kafka-based event-driven architecture.

## ✨ Key Features

### Core Platform Features
- **User Management**: Complete authentication and authorization system with Spring Security
- **Content Creation**: Users can create posts, comments, and replies with rich text support
- **Hybrid Avatar System**: Support for both preloaded system avatars and custom user uploads
- **Profile Management**: Comprehensive user profile system with avatar integration
- **Legal Text Management**: Dynamic management of terms of service, privacy policies, etc.

### Advanced Features
- **Real-time Moderation**: Automated content moderation with Kafka-based event processing
- **Notification System**: Multi-channel notifications (email, push) for user engagement
- **Audit System**: Complete audit trail for all user actions and system events
- **RESTful API**: Comprehensive API with OpenAPI documentation
- **Multi-environment Support**: Development, testing, and production configurations

## 🏗️ Technical Architecture

### Event-Driven Architecture
```
User Action → Service Layer → Database → Kafka Event → 
Moderation Pipeline → Notification Dispatch → Admin Alerts
```

### Core Technologies
- **Java 17+**: Modern Java with latest features
- **Spring Boot 3.x**: Comprehensive framework for rapid development
- **Spring Data JPA**: Simplified data access layer
- **Spring Security**: Enterprise-grade security framework
- **Apache Kafka**: Event streaming platform for real-time processing
- **MySQL/H2**: Flexible database support for different environments
- **Lombok**: Reduces boilerplate code
- **Springdoc OpenAPI**: Automatic API documentation generation

### Kafka Integration
- **Event Streaming**: Real-time processing of user actions
- **Topic Management**: Separate topics for comments, replies, emails, and notifications
- **Dual Configuration**: Supports both Kafka-enabled and disabled modes
- **Message Durability**: Guaranteed message delivery with retry mechanisms

## 🚀 Quick Start

### Prerequisites
- Java 17 or higher
- Maven 3.6+ for dependency management
- MySQL 8.0+ (or H2 for development)
- Apache Kafka 2.8+ (optional, for full features)

### Installation
1. Clone the repository
2. Configure database connection in `application.properties`
3. Run `mvn clean install` to build the project
4. Start the application with `java -jar target/agora_web-0.0.1-SNAPSHOT.jar`

### Development Scripts
- `start-dev.bat` - Development mode with H2 database
- `start-mysql.bat` - Production mode with MySQL
- `start-with-notifications.bat` - Full mode with Kafka notifications

## 📋 API Endpoints

### Avatar Management
- `GET /api/v1/any/avatars` - List all available avatars
- `GET /api/v1/any/avatars/{id}` - Get specific avatar
- `POST /api/v1/any/avatars/upload` - Upload custom avatar
- `DELETE /api/v1/any/avatars/{id}` - Delete custom avatar

### Profile Management
- `PUT /api/v1/profiles/{id}` - Update user profile
- `GET /api/v1/profiles/{id}` - Get user profile with avatar

### Content Management
- `POST /api/v1/posts` - Create new post
- `POST /api/v1/comments` - Create comment on post
- `POST /api/v1/replies` - Create reply to comment

### Legal Text Management
- `GET /api/v1/legal-texts/{type}` - Get legal text by type
- `PUT /api/v1/legal-texts/{type}` - Update legal text

## 🧪 Testing

### Test Categories
- **Unit Tests**: Individual component testing
- **Integration Tests**: End-to-end workflow validation
- **Kafka Tests**: Message flow and processing verification
- **Audit Tests**: System health and compliance checks

### Running Tests
```bash
# Run all tests
mvn test

# Run specific test categories
mvn test -Dtest="**/*KafkaTest"
mvn test -Dtest="**/*IntegrationTest"
mvn test -Dtest="**/*AuditTest"
```

## 📊 Monitoring & Observability

### Logging
- Structured JSON logging for production environments
- Comprehensive error tracking and debugging information
- Request correlation IDs for distributed tracing

### Health Checks
- Application health endpoints
- Database connectivity monitoring
- Kafka connection status (when enabled)

## 🔒 Security Features

### Authentication & Authorization
- JWT-based authentication system
- Role-based access control (RBAC)
- Secure password hashing with BCrypt
- Session management and timeout controls

### Data Protection
- Input validation and sanitization
- XSS and SQL injection protection
- CORS configuration for frontend integration
- Audit logging for all sensitive operations

## 📈 Performance & Scalability

### Optimization Features
- Asynchronous processing with Kafka
- Efficient database queries with JPA
- Lazy loading for related entities
- Connection pooling for database access

### Scalability Design
- Horizontal scaling support with Kafka partitioning
- Stateless service architecture
- Microservice-ready modular design
- Container deployment support

## 🔧 Configuration

### Application Properties
```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/agora_db
spring.datasource.username=your_username
spring.datasource.password=your_password

# Kafka Configuration (optional)
kafka.enabled=true
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.consumer.group-id=agora-comment-group

# Security Configuration
jwt.secret=your-secret-key
jwt.expiration=86400000
```

## 📚 Documentation

### Additional Resources
- [`KAFKA_IMPLEMENTATION_COMPLETE.md`](KAFKA_IMPLEMENTATION_COMPLETE.md) - Detailed Kafka implementation guide
- [`IMPLEMENTATION_SUMMARY.md`](IMPLEMENTATION_SUMMARY.md) - Complete implementation overview
- [`AVATAR_SYSTEM.md`](AVATAR_SYSTEM.md) - Avatar system documentation
- [`SECURITY.md`](SECURITY.md) - Security implementation details

### API Documentation
- OpenAPI documentation available at `/swagger-ui.html` when running
- Comprehensive endpoint documentation with examples
- Request/response schemas and validation rules

## 🎯 Development Status

### ✅ Completed Features
- [x] User authentication and authorization
- [x] Post, comment, and reply management
- [x] Hybrid avatar system (preloaded + custom)
- [x] Profile management with avatar integration
- [x] Legal text management system
- [x] Kafka-based event processing
- [x] Email and push notification system
- [x] Comprehensive testing framework
- [x] Security implementation
- [x] API documentation

### 🔄 Ongoing Enhancements
- [ ] Real-time WebSocket integration
- [ ] Advanced analytics dashboard
- [ ] Mobile API optimization
- [ ] Multi-language support

## 👥 Contributing

### Development Guidelines
1. Follow Spring Boot best practices
2. Maintain comprehensive test coverage
3. Document all public APIs
4. Use conventional commit messages
5. Ensure security compliance

### Code Style
- Java 17+ features encouraged
- Clean Architecture principles
- SOLID design principles
- Comprehensive error handling

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 📞 Support

For technical support or questions:
- Review the documentation in this repository
- Check the issues page for common problems
- Contact the development team for assistance

---

**Status**: ✅ Production Ready  
**Version**: 1.0.0  
**Last Updated**: July 1, 2025
MySQL para la base de datos.
Instalación y Ejecución
Clonar el repositorio:
   git clone https://github.com/tu_usuario/agora_web.git
Configurar la base de datos: Asegúrate de tener una instancia de MySQL en ejecución y configurada con las credenciales correctas en el archivo application.properties.
Construir y ejecutar la aplicación:
   mvn clean install
   mvn spring-boot:run
Documentación de la API
La documentación de la API generada por Springdoc OpenAPI está disponible en la ruta /swagger-ui.html de la aplicación.

Contribuciones
Las contribuciones son bienvenidas. Por favor, lee el archivo CONTRIBUTING.md para obtener más detalles sobre cómo contribuir al proyecto.

Uername Y password 
Username administrador: admin
Password administrador: admin
Username user1: user1
Password user1: user1
Username user2: user2
Password user2: user2
Username user3: user3
Password user3: user3

