# Arquitectura Optimizada - Separación de Responsabilidades

## Resumen de la Optimización
Se ha completado la optimización de la arquitectura de la API para cumplir con la separación de responsabilidades, evitar respuestas JSON recursivas/infernales, y cumplir con GDPR (derecho al olvido).

## Controladores y sus Responsabilidades

### 1. UserController (`/api/v1/any/user`)
**Responsabilidad:** CRUD básico de usuarios y registro únicamente

#### Endpoints disponibles:
- `GET /user` - Listar todos los usuarios (devuelve UserListDTO)
- `GET /user/{userId}` - Obtener usuario por ID (devuelve UserListDTO)
- `POST /user` - Crear usuario (devuelve UserListDTO)
- `POST /user/register` - Registro de usuario (devuelve String mensaje)
- `DELETE /user/{userId}` - Eliminación administrativa con GDPR (solo ADMIN)

#### ✅ LO QUE YA NO ESTÁ AQUÍ:
- ❌ Edición de perfil (`PUT /user/me` o `PUT /user/{userId}`)
- ❌ Auto-eliminación de usuario (`DELETE /user/me`) 

### 2. ProfileController (`/api/v1/any/user/profile`)
**Responsabilidad:** Gestión completa de perfiles de usuario

#### Endpoints disponibles:
- `GET /user/profile/{id}` - Obtener perfil por ID
- `PUT /user/profile/{id}` - Actualizar perfil (por ID, para admin)
- `DELETE /user/profile/{id}` - Eliminar perfil (por ID, para admin)
- `PUT /user/profile/me` - ✅ **NUEVO** Auto-edición de perfil (usuario logueado)
- `DELETE /user/profile/me` - ✅ **NUEVO** Auto-eliminación con GDPR (usuario logueado)
- Endpoints de favoritos: `GET|PUT|DELETE|POST /user/profile/favorite/{id}`

### 3. BannedController (`/api/v1/admin/banned`)
**Responsabilidad:** Gestión exclusiva de baneos (solo administradores)

#### Endpoints disponibles:
- `GET /banned` - Listar usuarios baneados
- `POST /banned/{userId}` - Banear usuario
- `DELETE /banned/{userId}` - Desbanear usuario
- `GET /banned/{userId}` - Verificar si usuario está baneado

## Flujos de Frontend

### Para Editar Perfil
```javascript
// ✅ CORRECTO - Apuntar a ProfileController
// Auto-edición (usuario logueado)
PUT /api/v1/any/user/profile/me
{
  "firstName": "Nuevo Nombre",
  "lastName1": "Nuevo Apellido",
  "email": "nuevo@email.com",
  "city": "Nueva Ciudad"
}

// Edición administrativa (admin)
PUT /api/v1/any/user/profile/{profileId}
```

### Para Eliminar Cuenta (GDPR)
```javascript
// ✅ CORRECTO - Apuntar a ProfileController
// Auto-eliminación (usuario logueado)
DELETE /api/v1/any/user/profile/me

// Eliminación administrativa (admin)
DELETE /api/v1/any/user/{userId}  // UserController para admin
```

### Para Gestión de Usuarios (Admin)
```javascript
// ✅ CORRECTO - CRUD básico en UserController
GET /api/v1/any/user                    // Listar usuarios
GET /api/v1/any/user/{userId}           // Ver usuario
POST /api/v1/any/user                   // Crear usuario
DELETE /api/v1/any/user/{userId}        // Eliminar usuario (GDPR)

// ✅ CORRECTO - Baneos en BannedController
GET /api/v1/admin/banned                // Listar baneados
POST /api/v1/admin/banned/{userId}      // Banear usuario
DELETE /api/v1/admin/banned/{userId}    // Desbanear usuario
```

## Beneficios Obtenidos

### 1. ✅ JSON Optimizado
- **Antes:** Respuestas recursivas infinitas con entidades completas
- **Ahora:** DTOs optimizados (`UserListDTO`, `ProfileDTO`) que evitan recursión

### 2. ✅ Separación de Responsabilidades
- **UserController:** Solo CRUD básico + registro
- **ProfileController:** Solo gestión de perfiles
- **BannedController:** Solo gestión de baneos

### 3. ✅ GDPR Compliant
- Eliminación en cascada coordinated por `GdprService`
- Elimina: usuario, perfil, comentarios, replies, posts, bans
- Protección: No permite eliminar el último administrador

### 4. ✅ Seguridad Mejorada
- Auto-edición/eliminación con verificación de usuario logueado
- Endpoints administrativos protegidos con `@PreAuthorize("hasRole('ROLE_ADMIN')")` 
- Verificación de permisos en cada operación

## Cambios Requeridos en Frontend

### ❌ ANTES (Problemático):
```javascript
// Frontend apuntaba incorrectamente a UserController para todo
PUT /api/v1/any/user/me          // ❌ Ya no existe
DELETE /api/v1/any/user/me       // ❌ Ya no existe  
PUT /api/v1/any/user/{userId}    // ❌ Ya no existe
```

### ✅ AHORA (Correcto):
```javascript
// Apuntar a ProfileController para gestión de perfiles
PUT /api/v1/any/user/profile/me     // ✅ Auto-edición
DELETE /api/v1/any/user/profile/me  // ✅ Auto-eliminación GDPR

// UserController solo para CRUD básico
GET /api/v1/any/user               // ✅ Listar usuarios  
POST /api/v1/any/user/register     // ✅ Registro
DELETE /api/v1/any/user/{userId}   // ✅ Eliminación admin (GDPR)

// BannedController para baneos
POST /api/v1/admin/banned/{userId}   // ✅ Banear
DELETE /api/v1/admin/banned/{userId} // ✅ Desbanear
```

## Servicios de Respaldo

### GdprService
- **Función:** Coordina eliminación en cascada para cumplimiento GDPR
- **Elimina:** Comentarios, Replies, Posts (archiva), Bans, Perfil, Usuario
- **Protección:** Verifica que no sea el último administrador

### DTOs Utilizados  
- **UserListDTO:** Respuesta optimizada sin recursión para usuarios
- **ProfileDTO:** Respuesta optimizada para perfiles
- **UserUpdateDTO:** Datos de entrada para actualizaciones (solo en UserController)

## Estado Final
✅ **Arquitectura limpia y optimizada**
✅ **Sin respuestas JSON recursivas** 
✅ **Separación total de responsabilidades**
✅ **GDPR completamente implementado**
✅ **Eliminación en cascada funcionando**
✅ **Frontend debe apuntar a endpoints correctos según responsabilidad**
