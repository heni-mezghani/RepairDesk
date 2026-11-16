package com.repair_desk_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;


/**
 * One place that turns exceptions into clean, consistent JSON error responses, so the
 * front-end always receives the same shape no matter what went wrong:
 *
 * <pre>
 * {
 *   "timestamp": "23-06-2026 14:31:05",
 *   "status": 404,
 *   "error": "Not Found",
 *   "message": "Client introuvable : 7",
 *   "path": "/api/clients/7"
 * }
 * </pre>
 *
 * <p>For validation errors a sixth field, "fieldErrors", lists the problem per field.</p>
 *
 * <p>We build the body as a {@code Map} on purpose: the project rule allows DTOs only
 * for login, so we avoid creating an error-response DTO class.</p>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404 — something was looked up but does not exist. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    /** 401 — wrong username or password at login. */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentials(BadCredentialsException ex,
                                                                    HttpServletRequest request) {
        return build(HttpStatus.UNAUTHORIZED, "Nom d'utilisateur ou mot de passe incorrect", request);

    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<Map<String, Object>> handleDisabled(DisabledException ex,
                                                              HttpServletRequest request) {
        return build(HttpStatus.FORBIDDEN, "Ce compte a été désactivé. Contactez l'administrateur.", request);
    }


    /** 400 — bean validation (@Valid) failed; we report the message for each bad field. */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        Map<String, String> fieldErrors = new LinkedHashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            fieldErrors.put(error.getField(), error.getDefaultMessage());

        }

        Map<String, Object> body = baseBody(HttpStatus.BAD_REQUEST, "Données invalides", request);
        body.put("fieldErrors", fieldErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /** 500 — anything we did not anticipate. Keep the message generic for safety. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("500 error: ", ex);
        // TODO (intern): log 'ex' here (e.g. with SLF4J) so you can debug 500s.
        return build(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne est survenue", request);
    }

    // ----------------------------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------------------------

    /** Build the standard body and wrap it in a ResponseEntity with the matching status. */
    private ResponseEntity<Map<String, Object>> build(HttpStatus status, String message,
                                                      HttpServletRequest request) {
        return ResponseEntity.status(status).body(baseBody(status, message, request));
    }

    /** The five common fields shared by every error response. */
    private Map<String, Object> baseBody(HttpStatus status, String message, HttpServletRequest request) {
        Map<String, Object> body = new LinkedHashMap<>(); // LinkedHashMap keeps the field order
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        body.put("path", request.getRequestURI());
        return body;
    }
}
