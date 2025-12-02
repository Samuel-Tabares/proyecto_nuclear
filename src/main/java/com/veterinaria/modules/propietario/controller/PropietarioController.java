package com.veterinaria.modules.propietario.controller;

import com.veterinaria.modules.propietario.dto.PropietarioDTO;
import com.veterinaria.modules.propietario.service.PropietarioService;
import com.veterinaria.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Propietarios
 * Endpoints: /api/propietarios
 */
@RestController
@RequestMapping("/api/propietarios")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Para desarrollo con frontend
public class PropietarioController {

    private final PropietarioService service;

    @PostMapping
    public ResponseEntity<ApiResponse<PropietarioDTO>> crear(@Valid @RequestBody PropietarioDTO dto) {
        PropietarioDTO creado = service.crear(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(creado, "Propietario registrado exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PropietarioDTO>>> listar() {
        List<PropietarioDTO> propietarios = service.listarTodos();
        return ResponseEntity.ok(ApiResponse.success(propietarios, "Propietarios obtenidos"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PropietarioDTO>> obtener(@PathVariable Long id) {
        PropietarioDTO propietario = service.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(propietario, "Propietario encontrado"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PropietarioDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PropietarioDTO dto) {
        PropietarioDTO actualizado = service.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizado, "Propietario actualizado"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Propietario eliminado"));
    }
}