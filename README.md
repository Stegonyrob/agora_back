Agora Web
Agora Web es una plataforma web que permite a los usuarios crear publicaciones, responder a publicaciones y gestionar sus cuentas. Este proyecto está construido con Java Spring Boot y utiliza una variedad de tecnologías y herramientas para proporcionar una experiencia de usuario segura y eficiente.

Características Principales
Autenticación y Autorización: Utiliza Spring Security para manejar la autenticación y autorización de los usuarios.
Gestión de Usuarios: Permite el registro de usuarios, la gestión de cuentas y la asignación de roles.
Publicaciones y Respuestas: Los usuarios pueden crear publicaciones y responder a las publicaciones existentes.
API RESTful: Proporciona una API RESTful para interactuar con la aplicación, incluyendo operaciones CRUD para publicaciones y respuestas.
Seguridad: Implementa medidas de seguridad para proteger los datos de los usuarios y asegurar la integridad de la aplicación.
Tecnologías Utilizadas
Java: Lenguaje de programación principal para el desarrollo del backend.
Spring Boot: Framework para crear aplicaciones Spring de manera rápida y fácil.
Spring Data JPA: Simplifica la implementación de capas de acceso a datos.
Spring Security: Proporciona autenticación, autorización y otras características de seguridad.
MySQL: Sistema de gestión de bases de datos para almacenar datos de la aplicación.
Lombok: Reduce el código repetitivo en las clases de modelo.
Springdoc OpenAPI: Genera documentación de la API en formato OpenAPI.
Requisitos
Java 11 o superior.
Maven para la gestión de dependencias y la construcción del proyecto.
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



Uername Y password 
Username administrador: admin
Password administrador: admin
Username user1: user1
Password user1: user1
Username user2: user2
Password user2: user2
Username user3: user3
Password user3: user3

