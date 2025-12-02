package com.veterinaria.modules.cita.controller;

import com.veterinaria.modules.cita.dto.CitaDTO;
import com.veterinaria.modules.cita.facade.CitaFacade;
import com.veterinaria.modules.cita.service.CitaService;
import com.veterinaria.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para Citas
 * Endpoints: /api/citas
 * Usa CitaFacade para operaciones complejas
 */
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CitaController {

    private final CitaFacade citaFacade;
    private final CitaService citaService;

    @PostMapping
    public ResponseEntity<ApiResponse<CitaDTO>> crear(@Valid @RequestBody CitaDTO dto) {
        CitaDTO creada = citaFacade.crearCitaConNotificacion(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(creada, "Cita agendada y notificación enviada"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CitaDTO>>> listar() {
        List<CitaDTO> citas = citaService.listarTodas();
        return ResponseEntity.ok(ApiResponse.success(citas, "Citas obtenidas"));
    }

    @GetMapping("/mascota/{mascotaId}")
    public ResponseEntity<ApiResponse<List<CitaDTO>>> listarPorMascota(@PathVariable Long mascotaId) {
        List<CitaDTO> citas = citaService.listarPorMascota(mascotaId);
        return ResponseEntity.ok(ApiResponse.success(citas, "Citas de la mascota"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaDTO>> obtener(@PathVariable Long id) {
        CitaDTO cita = citaService.obtenerPorId(id);
        return ResponseEntity.ok(ApiResponse.success(cita, "Cita encontrada"));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CitaDTO>> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody CitaDTO dto) {
        CitaDTO actualizada = citaFacade.actualizarCitaConNotificacion(id, dto);
        return ResponseEntity.ok(ApiResponse.success(actualizada, "Cita actualizada"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminar(@PathVariable Long id) {
        citaService.eliminar(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Cita eliminada"));
    }
}