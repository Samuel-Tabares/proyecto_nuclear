package com.veterinaria.modules.propietario.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferencia de datos de Propietario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropietarioDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "Nombre entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(min = 2, max = 100, message = "Apellido entre 2 y 100 caracteres")
    private String apellido;

    @NotBlank(message = "El documento es obligatorio")
    @Size(min = 5, max = 20, message = "Documento entre 5 y 20 caracteres")
    private String documento;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(min = 7, max = 15, message = "Teléfono entre 7 y 15 caracteres")
    private String telefono;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Email inválido")
    private String email;

    private String direccion;
    private String fechaRegistro;
}