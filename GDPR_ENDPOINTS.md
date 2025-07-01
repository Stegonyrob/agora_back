# Endpoints de Cumplimiento GDPR - Protección de Datos Europea

## Nuevos Endpoints Implementados

### 1. Auto-eliminación de Cuenta (GDPR)

**Endpoint:** `DELETE /api/v1/any/user/me`

**Descripción:** Permite que un usuario elimine su propia cuenta y todos sus datos permanentemente.

**Autenticación:** Requiere `ROLE_USER` (cualquier usuario autenticado)

**Headers:**
```
Authorization: Bearer {token_del_usuario}
```

**Body:** No requiere

**Respuesta Exitosa (200):**
```json
"Tu cuenta y todos tus datos han sido eliminados permanentemente conforme al GDPR"
```

**Protecciones:**
- No permite eliminar el último administrador del sistema
- Solo el usuario autenticado puede eliminar su propia cuenta
- Eliminación completa de todos los datos asociados

**Ejemplo JavaScript:**
```javascript
const deleteMyAccount = async (token) => {
  const response = await fetch('/api/v1/any/user/me', {
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

### 2. Auto-edición de Perfil (GDPR)

**Endpoint:** `PUT /api/v1/any/user/me`

**Descripción:** Permite que un usuario edite su propia información personal.

**Autenticación:** Requiere `ROLE_USER` (cualquier usuario autenticado)

**Headers:**
```
Content-Type: application/json
Authorization: Bearer {token_del_usuario}
```

**Body (JSON):**
```json
{
  "username": "nuevo_username",
  "email": "nuevo_email@ejemplo.com",
  "firstName": "Nuevo Nombre",
  "lastName1": "Nuevo Apellido1",
  "lastName2": "Nuevo Apellido2",
  "password": "nueva_contraseña_opcional",
  "acceptedRules": true
}
```

**Campos Opcionales:** Todos los campos son opcionales. Solo se actualizarán los que se envíen.

**Respuesta Exitosa (200):**
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
  "roles": ["ROLE_USER"],
  "isBanned": false,
  "banReason": null,
  "fullName": "Nuevo Nombre Nuevo Apellido1 Nuevo Apellido2",
  "admin": false
}
```

**Restricciones:**
- Un usuario no puede cambiar sus propios roles (solo admin puede hacerlo)
- El username debe ser único
- El email debe tener formato válido

**Ejemplo JavaScript:**
```javascript
const updateMyProfile = async (userData, token) => {
  const response = await fetch('/api/v1/any/user/me', {
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
      acceptedRules: userData.acceptedRules
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

## Endpoints Administrativos (Ya Existentes)

### 3. Eliminar Usuario (Admin)

**Endpoint:** `DELETE /api/v1/any/user/{userId}`

**Descripción:** Permite que un administrador elimine cualquier usuario.

**Autenticación:** Requiere `ROLE_ADMIN`

**Uso:** Para casos de usuarios que incumplen normas reiteradamente.

### 4. Actualizar Usuario (Admin)

**Endpoint:** `PUT /api/v1/any/user/{userId}`

**Descripción:** Permite que un administrador actualice cualquier usuario, incluyendo roles.

**Autenticación:** Requiere `ROLE_ADMIN`

## Cumplimiento GDPR

### Derechos del Usuario Implementados:

1. **Derecho de Acceso:** ✅ `GET /api/v1/any/user/me` (obtener información propia)
2. **Derecho de Rectificación:** ✅ `PUT /api/v1/any/user/me` (corregir información propia)
3. **Derecho de Supresión:** ✅ `DELETE /api/v1/any/user/me` (eliminar cuenta propia)
4. **Derecho de Portabilidad:** ✅ Los datos se devuelven en formato JSON estructurado

### Flujo de Gestión de Usuarios:

1. **Usuario Normal:**
   - Se registra: `POST /api/v1/any/user/register`
   - Edita su perfil: `PUT /api/v1/any/user/me`
   - Elimina su cuenta: `DELETE /api/v1/any/user/me`

2. **Administrador:**
   - Ve todos los usuarios: `GET /api/v1/any/user`
   - Ve usuario específico: `GET /api/v1/any/user/{userId}`
   - Edita cualquier usuario: `PUT /api/v1/any/user/{userId}`
   - Elimina usuario problemático: `DELETE /api/v1/any/user/{userId}`
   - Banea usuario: Usar `BannedController` (módulo separado)

### Protecciones Implementadas:

- **Último Administrador:** No se puede eliminar el último admin del sistema
- **Auto-gestión:** Usuarios solo pueden gestionar su propia cuenta
- **Autenticación:** Todos los endpoints requieren autenticación válida
- **Autorización:** Separación clara entre permisos de usuario y admin

## Integración con Frontend

### Página de Perfil de Usuario:
```javascript
// Cargar perfil actual
const loadMyProfile = async () => {
  const response = await fetch('/api/v1/any/user/me', {
    headers: { 'Authorization': `Bearer ${token}` }
  });
  return response.json();
};

// Actualizar perfil
const saveProfile = async (formData) => {
  return await updateMyProfile(formData, token);
};

// Eliminar cuenta
const deleteAccount = async () => {
  if (confirm('¿Estás seguro? Esta acción no se puede deshacer.')) {
    return await deleteMyAccount(token);
  }
};
```

### Panel de Administrador:
```javascript
// Usar los endpoints administrativos existentes
// PUT /api/v1/any/user/{userId} para editar
// DELETE /api/v1/any/user/{userId} para eliminar
```

## Notas Importantes

1. **Eliminación Permanente:** Ambos endpoints de eliminación borran completamente los datos del usuario
2. **Sin Recuperación:** No hay papelera ni recuperación de cuentas eliminadas
3. **Logs de Auditoría:** Considerar implementar logs para cumplimiento legal
4. **Notificaciones:** El frontend debería mostrar advertencias claras antes de eliminar
5. **Backup:** Asegurar que los backups también cumplan con GDPR (eliminación de datos cuando sea necesario)
