package com.veterinaria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación veterinaria
 * Inicia el contexto de Spring Boot con arquitectura modular
 */
@SpringBootApplication
public class VetAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(VetAppApplication.class, args);
    }
}