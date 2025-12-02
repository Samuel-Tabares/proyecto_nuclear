package com.veterinaria.shared.exception;

/**
 * Excepción para recursos no encontrados (404)
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

/**
 * Excepción para errores de lógica de negocio (400)
 */
class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}