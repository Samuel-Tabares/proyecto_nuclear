package com.veterinaria.modules.historia.controller;

import com.veterinaria.modules.historia.dto.HistoriaClinicaDTO;
import com.veterinaria.modules.historia.service.HistoriaClinicaService;
import com.veterinaria.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Historias Clínicas
 * Endpoints: /api/historias
 */
@RestController
@RequestMapping("/api/historias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class HistoriaClinicaController {

    private final HistoriaClinicaService service;

    @PostMapping
    public ResponseEntity<ApiResponse<HistoriaClinicaDTO>> crear(@Valid @RequestBody HistoriaClinicaDTO dto) {
        HistoriaClinicaDTO creada = service.crear(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(creada, "Historia clínica creada exitosamente"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<HistoriaClinicaDTO>>> listar() {
        List<HistoriaClinicaDTO> historias = service.listarTodas();
        return ResponseEntity.ok(ApiResponse.success(historias, "Historias clínicas obtenidas"));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<ApiResponse<List<HistoriaClinicaDTO>>> listarPorMascota(
            @PathVariable Long mascotaId) {
        List<HistoriaClinicaDTO> historias = service.listarPorMascota(mascotaId);
        return ResponseEntity.ok(ApiResponse.success(historias, "Historias de la mascota"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoriaClinicaDTO>> obtener(@PathVariable Long id) {
        HistoriaClinicaDTO historia = service.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(historia, "Historia clínica encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<HistoriaClinicaDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody HistoriaClinicaDTO dto) {
        HistoriaClinicaDTO actualizada = service.actualizar(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Historia clínica actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Historia clínica eliminada"));
    }
}