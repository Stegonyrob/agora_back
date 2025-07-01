# Endpoints Administrativos de Usuarios - Guía de Uso

## URLs de los Endpoints

Asumiendo que `api-endpoint` = `/api/v1`:

- **PUT** (Actualizar): `/api/v1/any/user/{userId}`
- **DELETE** (Eliminar): `/api/v1/any/user/{userId}`

## Autenticación Requerida

Ambos endpoints requieren:
- **Rol**: `ROLE_ADMIN`
- **Token JWT**: Incluir en header `Authorization: Bearer {token}`

## 1. PUT - Actualizar Usuario

### URL
```
PUT /api/v1/any/user/{userId}
```

### Headers
```
Content-Type: application/json
Authorization: Bearer {tu_token_jwt_admin}
```

### Formato del Body (JSON)

```json
{
  "username": "nuevo_username",
  "email": "nuevo_email@ejemplo.com",
  "firstName": "Nuevo Nombre",
  "lastName1": "Nuevo Apellido1",
  "lastName2": "Nuevo Apellido2",
  "roles": ["ROLE_USER", "ROLE_ADMIN"],
  "password": "nueva_contraseña_opcional",
  "acceptedRules": true
}
```

### Campos Obligatorios
- `username` (string): Nombre de usuario único
- `email` (string): Email válido
- `acceptedRules` (boolean): Si ha aceptado las reglas

### Campos Opcionales
- `firstName` (string): Nombre
- `lastName1` (string): Primer apellido  
- `lastName2` (string): Segundo apellido
- `roles` (array): Array de strings con los roles
- `password` (string): Solo si quieres cambiar la contraseña

### Respuesta Exitosa (200)
```json
{
  "id": 123,
  "username": "nuevo_username",
  "email": "nuevo_email@ejemplo.com",
  "acceptedRules": true,
  "firstName": "Nuevo Nombre",
  "lastName1": "Nuevo Apellido1",
  "lastName2": "Nuevo Apellido2",
  "avatarId": 1,
  "avatarUrl": "/avatars/default.png",
  "avatarDisplayName": "Avatar por defecto",
  "roles": ["ROLE_USER", "ROLE_ADMIN"],
  "isBanned": false,
  "banReason": null,
  "fullName": "Nuevo Nombre Nuevo Apellido1 Nuevo Apellido2",
  "admin": true
}
```

### Respuestas de Error
- **400 Bad Request**: Datos inválidos o error de validación
- **401 Unauthorized**: Token inválido o expirado
- **403 Forbidden**: No tienes permisos de administrador  
- **404 Not Found**: Usuario no encontrado

## 2. DELETE - Eliminar Usuario

### URL
```
DELETE /api/v1/any/user/{userId}
```

### Headers
```
Authorization: Bearer {tu_token_jwt_admin}
```

### Body
No requiere body (solo el ID en la URL)

### Respuesta Exitosa (200)
```json
"Usuario eliminado exitosamente"
```

### Respuestas de Error
- **400 Bad Request**: "No se puede eliminar el último administrador del sistema"
- **401 Unauthorized**: Token inválido o expirado
- **403 Forbidden**: No tienes permisos de administrador
- **404 Not Found**: Usuario no encontrado

## Ejemplos de Uso con JavaScript

### Actualizar Usuario
```javascript
const updateUser = async (userId, userData, token) => {
  const response = await fetch(`/api/v1/any/user/${userId}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    },
    body: JSON.stringify({
      username: userData.username,
      email: userData.email,
      firstName: userData.firstName,
      lastName1: userData.lastName1,
      lastName2: userData.lastName2,
      roles: userData.roles,
      acceptedRules: userData.acceptedRules,
      // password: userData.password // Solo si quieres cambiarla
    })
  });

  if (response.ok) {
    return await response.json();
  } else {
    throw new Error(`Error ${response.status}: ${await response.text()}`);
  }
};
```

### Eliminar Usuario
```javascript
const deleteUser = async (userId, token) => {
  const response = await fetch(`/api/v1/any/user/${userId}`, {
    method: 'DELETE',
    headers: {
      'Authorization': `Bearer ${token}`
    }
  });

  if (response.ok) {
    return await response.text();
  } else {
    throw new Error(`Error ${response.status}: ${await response.text()}`);
  }
};
```

## Ejemplos con curl

### Actualizar Usuario
```bash
curl -X PUT http://localhost:8080/api/v1/any/user/123 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer tu_token_aqui" \
  -d '{
    "username": "nuevo_username",
    "email": "nuevo@ejemplo.com",
    "firstName": "Nuevo",
    "lastName1": "Apellido",
    "acceptedRules": true
  }'
```

### Eliminar Usuario
```bash
curl -X DELETE http://localhost:8080/api/v1/any/user/123 \
  -H "Authorization: Bearer tu_token_aqui"
```

## Notas Importantes

1. **Protección del último admin**: No se puede eliminar el último administrador del sistema
2. **Contraseña opcional**: En PUT, el campo `password` es opcional. Si no se envía, la contraseña actual se mantiene
3. **Validación**: El username debe ser único, el email debe tener formato válido
4. **Roles**: Por ahora la actualización de roles está comentada (TODO), pero el campo se puede enviar
5. **Perfil**: Si el usuario no tiene perfil, los campos firstName, lastName1, lastName2 se ignorarán

## Solución al Error 500

El error 500 que estabas experimentando era porque:
1. **No existían los endpoints PUT y DELETE** en el controlador
2. **Faltaban las importaciones** necesarias (`@PreAuthorize`, `@PutMapping`, `@DeleteMapping`)
3. **No había DTOs específicos** para las operaciones de actualización

Ahora estos endpoints están implementados y funcionando correctamente.
