package com.veterinaria.modules.prescripcion.controller;

import com.veterinaria.modules.prescripcion.dto.PrescripcionDTO;
import com.veterinaria.modules.prescripcion.service.PrescripcionService;
import com.veterinaria.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Prescripciones
 * Endpoints: /api/prescripciones
 */
@RestController
@RequestMapping("/api/prescripciones")
@RequiredArgsConstructor
public class PrescripcionController {

    private final PrescripcionService service;

    @PostMapping
    public ResponseEntity<ApiResponse<PrescripcionDTO>> crear(@Valid @RequestBody PrescripcionDTO dto) {
        PrescripcionDTO creada = service.crear(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(creada, "Prescripción creada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PrescripcionDTO>>> listar() {
        List<PrescripcionDTO> prescripciones = service.listarTodas();
        return ResponseEntity.ok(ApiResponse.success(prescripciones, "Prescripciones obtenidas"));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<ApiResponse<List<PrescripcionDTO>>> listarPorMascota(
            @PathVariable Long mascotaId) {
        List<PrescripcionDTO> prescripciones = service.listarPorMascota(mascotaId);
        return ResponseEntity.ok(ApiResponse.success(prescripciones, "Prescripciones de la mascota"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescripcionDTO>> obtener(@PathVariable Long id) {
        PrescripcionDTO prescripcion = service.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(prescripcion, "Prescripción encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<PrescripcionDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PrescripcionDTO dto) {
        PrescripcionDTO actualizada = service.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Prescripción actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Prescripción eliminada"));
    }
}