package com.veterinaria.modules.mascota.controller;

import com.veterinaria.modules.mascota.dto.MascotaDTO;
import com.veterinaria.modules.mascota.service.MascotaService;
import com.veterinaria.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Mascotas
 * Endpoints: /api/mascotas
 */
@RestController
@RequestMapping("/api/mascotas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MascotaController {

    private final MascotaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<MascotaDTO>> crear(@Valid @RequestBody MascotaDTO dto) {
        MascotaDTO creada = service.crear(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(creada, "Mascota registrada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<MascotaDTO>>> listar() {
        List<MascotaDTO> mascotas = service.listarTodas();
        return ResponseEntity.ok(ApiResponse.success(mascotas, "Mascotas obtenidas"));
    }

    @GetMapping("/propietario/{propietarioId}")
    public ResponseEntity<ApiResponse<List<MascotaDTO>>> listarPorPropietario(
            @PathVariable Long propietarioId) {
        List<MascotaDTO> mascotas = service.listarPorPropietario(propietarioId);
        return ResponseEntity.ok(ApiResponse.success(mascotas, "Mascotas del propietario"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MascotaDTO>> obtener(@PathVariable Long id) {
        MascotaDTO mascota = service.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(mascota, "Mascota encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MascotaDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody MascotaDTO dto) {
        MascotaDTO actualizada = service.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Mascota actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Mascota eliminada"));
    }
}