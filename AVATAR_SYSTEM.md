# Sistema Híbrido de Avatares - Ágora

## Descripción General

El sistema de avatares de Ágora implementa un modelo híbrido que soporta tanto avatares precargados (estáticos) como avatares personalizados (subidos por el usuario).

## Tipos de Avatares

### 1. Avatares Precargados (Preloaded)
- **Almacenamiento**: Archivos estáticos en el directorio `public/avatars/static/` del frontend
- **Base de datos**: Solo se almacena la referencia (nombre del archivo) y metadatos
- **Características**:
  - `preloaded = true`
  - `imageData = null` (no se almacenan datos binarios)
  - `imageName` contiene el nombre del archivo (ej: "default-avatar.png")
  - Uno de ellos debe ser marcado como `isDefault = true`

### 2. Avatares Personalizados (Custom)
- **Almacenamiento**: Datos binarios en la base de datos
- **Características**:
  - `preloaded = false`
  - `imageData` contiene los datos binarios de la imagen
  - `imageName` contiene el nombre original del archivo subido
  - `isDefault = false` (nunca pueden ser avatares por defecto)

## Endpoints de la API (Protegidos)

**IMPORTANTE**: Todos los endpoints requieren autenticación (usuarios registrados y logueados).

### GET `/api/v1/avatars/selector`
Retorna la lista optimizada de avatares precargados para el selector del frontend.
**Autenticación requerida**: Sí
```json
[
  {
    "id": 1,
    "imageName": "default-avatar.png",
    "displayName": "Avatar por Defecto"
  }
]
```

### GET `/api/v1/avatars/default`
Retorna el avatar por defecto del sistema.
**Autenticación requerida**: Sí

### GET `/api/v1/avatars/{id}`
Retorna un avatar específico por ID.
**Autenticación requerida**: Sí

### GET `/api/v1/avatars/name/{imageName}`
Retorna un avatar específico por nombre de imagen.
**Autenticación requerida**: Sí

### GET `/api/v1/avatars/{id}/image`
Retorna los datos binarios de un avatar personalizado para mostrar la imagen.
**Autenticación requerida**: Sí
**Nota**: Solo funciona para avatares personalizados (no precargados)

### POST `/api/v1/avatars/upload`
Sube un nuevo avatar personalizado.
**Autenticación requerida**: Sí
- **Parámetros**: 
  - `file`: MultipartFile con la imagen
  - `displayName`: Nombre descriptivo (opcional)

### DELETE `/api/v1/avatars/{id}`
Elimina un avatar personalizado (no permite eliminar precargados o el avatar por defecto).
**Autenticación requerida**: Sí

## Estructura de la Base de Datos

```sql
CREATE TABLE avatars (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    image_name VARCHAR(255) NOT NULL,
    image_data LONGBLOB,
    preloaded BOOLEAN NOT NULL DEFAULT FALSE,
    is_default BOOLEAN NOT NULL DEFAULT FALSE,
    display_name VARCHAR(255)
);
```

## Integración con el Frontend

### Para Avatares Precargados
El frontend debe construir la URL como: `/avatars/static/{imageName}`

### Para Avatares Personalizados
El frontend debe usar la URL: `/api/v1/avatars/{id}/image` (requiere autenticación)

## Flujo de Selección de Avatar

1. **Cargar selector**: El frontend llama a `/api/avatars/selector`
2. **Mostrar opciones**: Muestra avatares precargados + opción "Subir personalizado"
3. **Selección precargado**: Frontend guarda la referencia del ID del avatar seleccionado
4. **Subida personalizada**: Frontend llama a `/api/avatars/upload` y guarda el ID retornado

## Validaciones y Restricciones

- Solo puede existir un avatar marcado como `isDefault = true`
- Los avatares precargados no pueden ser eliminados
- El avatar por defecto no puede ser eliminado
- Los avatares personalizados solo pueden ser eliminados por su propietario (implementar autorización)

## Archivos Involucrados

- **Entidad**: `Avatar.java`
- **Repository**: `AvatarRepository.java`
- **Service**: `IAvatarService.java`, `AvatarServiceImpl.java`
- **Controller**: `AvatarController.java`
- **DTOs**: `AvatarDTO.java`, `AvatarSelectorDTO.java`
- **Datos iniciales**: `data.sql`

## Consideraciones de Rendimiento

- Los avatares precargados se sirven directamente desde archivos estáticos (más rápido)
- Los avatares personalizados se sirven desde la base de datos (más lento pero necesario)
- Se recomienda implementar caché para avatares personalizados en producción

## Consideraciones de Seguridad

- Validar tipos de archivo permitidos (PNG, JPG, JPEG)
- Limitar tamaño máximo de archivo (ej: 2MB)
- Sanitizar nombres de archivo
- Implementar autorización para eliminar avatares personalizados
