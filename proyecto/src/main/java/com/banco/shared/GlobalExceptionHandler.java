package com.banco.shared;

import com.banco.domain.exception.EntityNotFoundException;
import com.banco.domain.exception.BusinessRuleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<Map<String,Object>> handleReglaNegocio(BusinessRuleException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(error(ex.getMessage(), 422));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String,Object>> handleNotFound(EntityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error(ex.getMessage(), 404));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String,Object>> handleAccess(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(error("No tiene permisos para realizar esta operación.", 403));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String,Object>> handleGeneral(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error("Error interno: " + ex.getMessage(), 500));
    }

    private Map<String,Object> error(String msg, int status) {
        return Map.of("message", msg, "status", status, "timestamp", LocalDateTime.now().toString());
    }
}
