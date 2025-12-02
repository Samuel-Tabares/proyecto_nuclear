package com.veterinaria.modules.cita.service;

import com.veterinaria.modules.cita.dto.CitaDTO;
import com.veterinaria.modules.cita.entity.Cita;
import com.veterinaria.modules.cita.repository.CitaRepository;
import com.veterinaria.modules.mascota.entity.Mascota;
import com.veterinaria.modules.mascota.repository.MascotaRepository;
import com.veterinaria.shared.exception.ResourceNotFoundException;
import com.veterinaria.shared.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para lógica de negocio de Cita
 * Patrón Service Layer
 */
@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final MascotaRepository mascotaRepository;

    @Transactional
    public CitaDTO crear(CitaDTO dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada: " + dto.getMascotaId()));

        Cita cita = toEntity(dto);
        cita.setMascota(mascota);

        Cita saved = citaRepository.save(cita);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarTodas() {
        return citaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<CitaDTO> listarPorMascota(Long mascotaId) {
        return citaRepository.findByMascotaId(mascotaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CitaDTO obtenerPorId(Long id) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + id));
        return toDTO(cita);
    }

    @Transactional
    public CitaDTO actualizar(Long id, CitaDTO dto) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cita no encontrada: " + id));

        cita.setFechaHora(dto.getFechaHora());
        cita.setMotivo(dto.getMotivo());
        cita.setObservaciones(dto.getObservaciones());

        if (dto.getEstado() != null) {
            cita.setEstado(Cita.EstadoCita.valueOf(dto.getEstado()));
        }

        return toDTO(citaRepository.save(cita));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cita no encontrada: " + id);
        }
        citaRepository.deleteById(id);
    }

    // Conversión Entity <-> DTO
    private CitaDTO toDTO(Cita entity) {
        CitaDTO dto = new CitaDTO();
        dto.setId(entity.getId());
        dto.setFechaHora(entity.getFechaHora());
        dto.setMotivo(entity.getMotivo());
        dto.setObservaciones(entity.getObservaciones());
        dto.setEstado(entity.getEstado().name());
        dto.setMascotaId(entity.getMascota().getId());
        dto.setMascotaNombre(entity.getMascota().getNombre());
        dto.setPropietarioNombre(
                entity.getMascota().getPropietario().getNombre() + " " +
                        entity.getMascota().getPropietario().getApellido()
        );
        dto.setPropietarioEmail(entity.getMascota().getPropietario().getEmail());
        dto.setFechaCreacion(DateUtils.formatDateTime(entity.getFechaCreacion()));
        return dto;
    }

    private Cita toEntity(CitaDTO dto) {
        Cita entity = new Cita();
        entity.setFechaHora(dto.getFechaHora());
        entity.setMotivo(dto.getMotivo());
        entity.setObservaciones(dto.getObservaciones());
        return entity;
    }
}