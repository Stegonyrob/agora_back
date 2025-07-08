# Corrección de Método HTTP en EventImageController

## Cambio Realizado

### Backend
Se corrigió el endpoint `/delete-multiple` en `EventImageController`:
- **ANTES**: `@PostMapping("/delete-multiple")`
- **DESPUÉS**: `@DeleteMapping("/delete-multiple")`

### Justificación
- Semánticamente, un endpoint que elimina recursos debe usar el método HTTP DELETE
- POST debería reservarse para creación o acciones que no sean idempotentes
- Mejora la adherencia a los principios REST

## Implicaciones para el Frontend

### Variable de Entorno Afectada
```
VITE_API_ENDPOINT_EVENT_IMAGES_DELETE_MULTIPLE=/api/v1/any/event-images/delete-multiple
```

### Cambio Requerido en el Código Frontend
El frontend debe cambiar de:
```javascript
// ANTES - Incorrecto
fetch(url, {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(imageIds)
})

// DESPUÉS - Correcto
fetch(url, {
  method: 'DELETE',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify(imageIds)
})
```

## Endpoint Completo
- **URL**: `/api/v1/any/event-images/delete-multiple`
- **Método**: `DELETE`
- **Autenticación**: Requerida (USER o ADMIN)
- **Body**: `List<Long>` (array de IDs de imágenes)
- **Respuesta**: `204 No Content` en éxito

## Estado de Otros Endpoints
Se verificó que no existen otras inconsistencias similares en el codebase:
- ✅ No hay otros `@PostMapping` para operaciones de eliminación
- ✅ No hay `@DeleteMapping` para operaciones de creación
- ✅ No hay `@GetMapping` para operaciones de modificación

## Testing
Se recomienda verificar que:
1. El endpoint funciona correctamente con método DELETE
2. La autenticación sigue funcionando
3. El frontend se actualiza para usar DELETE en lugar de POST
