# Documentación de endpoints y flujo 2FA TOTP para frontend

## 1. Creación de administrador
- **Endpoint:** `POST {api-endpoint}/admin/profile/admins`
- **Body:**
  ```json
  {
    "username": "admin1",
    "password": "...",
    "email": "...",
    "phone": "+34123456789"
  }
  ```
- **Respuesta:**
  ```json
  {
    "id": 1,
    "username": "admin1",
    "email": "...",
    "admin": true
  }
  ```
- **Notas:** El backend genera automáticamente el secreto TOTP y lo asocia al admin.

---

## 2. Obtener el secreto TOTP para configurar 2FA
- **Endpoint:** `GET {api-endpoint}/admin/profile/{id}/2fa-secret`
- **Respuesta:**
  - String: secreto TOTP en base64 (ejemplo: `QWxhZGRpbjpPcGVuU2VzYW1l`)
- **Uso frontend:**
  - Genera un QR con el siguiente formato URI:
    ```
    otpauth://totp/AGORA:{username}?secret={secret}&issuer=AGORA
    ```
  - Ejemplo en JS:
    ```js
    const uri = `otpauth://totp/AGORA:${username}?secret=${secret}&issuer=AGORA`;
    // Usa una librería QR para mostrarlo
    ```
  - El admin debe escanear el QR con Google Authenticator, Microsoft Authenticator, etc.

---

## 3. Validar código TOTP
- **Endpoint:** `POST {api-endpoint}/admin/profile/{id}/2fa-validate`
- **Body:**
  - String plano con el código TOTP (ejemplo: `"123456"`)
- **Respuesta:**
  - Boolean: `true` si el código es válido, `false` si no.
- **Uso frontend:**
  - Solicita al admin el código de su app 2FA y envíalo a este endpoint.
  - Si es válido, permite el acceso o la acción protegida.

---

## 4. Consideraciones y recomendaciones
- El secreto TOTP se genera y almacena automáticamente al crear el admin.
- El QR debe mostrarse solo una vez o bajo control de seguridad.
- Si el admin pierde acceso a su app 2FA, debe existir un mecanismo de recuperación (contacto soporte, etc).
- El backend valida el código TOTP con tolerancia de 1 ventana (30s antes/después).
- El secreto se devuelve en base64; si la librería QR requiere base32, conviértelo antes de generar el QR.

---

## 5. Resumen de endpoints
| Acción                        | Método | Endpoint                                    | Body/Params         | Respuesta         |
|-------------------------------|--------|---------------------------------------------|---------------------|-------------------|
| Crear admin                   | POST   | `/admin/profile/admins`                     | JSON                | AdminUserDTO      |
| Obtener secreto TOTP          | GET    | `/admin/profile/{id}/2fa-secret`            | -                   | String (secreto)  |
| Validar código TOTP           | POST   | `/admin/profile/{id}/2fa-validate`          | String (código)     | Boolean           |

---

## 6. Ejemplo de flujo frontend
1. Crear admin → mostrar QR generado con secreto → admin escanea y configura app 2FA.
2. Cuando el admin deba autenticarse o realizar acción sensible, pedir código 2FA y validarlo con el endpoint.

---

Para dudas o problemas, consulta al equipo backend.
