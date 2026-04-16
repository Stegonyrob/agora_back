package de.stella.agora_web.security.csrf.exception;

/**
 * 🚨 Excepción específica para problemas con tokens CSRF
 *
 * Se lanza cuando hay problemas en la gestión, validación o procesamiento de
 * tokens CSRF.
 *
 * Principios Clean Code: - Nombre descriptivo - Propósito específico - Herencia
 * apropiada
 */
public class CsrfTokenException extends RuntimeException {

    public CsrfTokenException(String message) {
        super(message);
    }

    public CsrfTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public CsrfTokenException(Throwable cause) {
        super(cause);
    }
}
