# Auditoría técnica: Checklist de producción y mejoras

## 1. Pruebas automatizadas
- Hay presencia de tests en `src/test/java`, pero muchos están comentados (`// @Test`).
- Hay tests activos en `TagControllerJsonTest`.
- Faltan tests activos y cobertura para controladores principales (eventos, posts, usuarios, imágenes).
- Recomendación: Reactivar y completar tests unitarios y de integración para todos los endpoints críticos.

## 2. Validaciones y manejo de errores
- No se encontró ningún `@ControllerAdvice` para manejo global de excepciones.
- Algunos controladores retornan errores genéricos (`500`), pero no mensajes claros ni validaciones exhaustivas.
- Recomendación: Implementar un handler global de errores y mejorar validaciones en DTOs y controladores.

## 3. Seguridad
- Uso correcto de `@PreAuthorize` en controladores sensibles (replies, imágenes, perfiles, settings, legal, etc.).
- Los endpoints públicos y privados están bien diferenciados (`/all/`, `/any/`).
- Recomendación: Revisar que todos los endpoints críticos tengan protección adecuada y roles bien definidos.

## 6. Optimización y limpieza
- Hay algunos `System.out.println` en código de seguridad, moderación, perfiles y notificaciones.
- Uso de `LOGGER` en algunos controladores, pero no de forma consistente.
- Hay comentarios `TODO` y código comentado en tests y otros archivos.
- Recomendación: Eliminar todos los `System.out.println`, usar solo logging estructurado (SLF4J/Logback), limpiar comentarios y código muerto.

## 8. Monitoreo y métricas
- No se detecta integración con sistemas de monitoreo (Prometheus, Grafana, Sentry, etc.).
- Hay mención de logging estructurado en el README, pero no evidencia de logs de auditoría ni métricas de salud.
- Recomendación: Integrar logs centralizados y métricas de salud para endpoints críticos.

---

## Resumen de lo que está bien
- Seguridad de endpoints con roles y rutas diferenciadas.
- Uso de DTOs para optimizar respuestas JSON.
- Lógica de tags robusta en posts y eventos.
- Configuración de logging en entorno de pruebas.

## Lo que se debe modificar antes de producción
- Reactivar y completar tests automatizados.
- Implementar manejo global de errores y validaciones exhaustivas.
- Eliminar `System.out.println` y comentarios/código muerto.
- Unificar logging con SLF4J/Logback.
- Integrar monitoreo y métricas de salud.

---

**Usa este checklist para priorizar tareas antes de pasar a producción.**
