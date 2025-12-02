package com.veterinaria.modules.prescripcion.service;

import com.veterinaria.modules.historia.entity.HistoriaClinica;
import com.veterinaria.modules.historia.repository.HistoriaClinicaRepository;
import com.veterinaria.modules.mascota.entity.Mascota;
import com.veterinaria.modules.mascota.repository.MascotaRepository;
import com.veterinaria.modules.prescripcion.dto.PrescripcionDTO;
import com.veterinaria.modules.prescripcion.entity.Prescripcion;
import com.veterinaria.modules.prescripcion.repository.PrescripcionRepository;
import com.veterinaria.shared.exception.ResourceNotFoundException;
import com.veterinaria.shared.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para lógica de negocio de Prescripcion
 * Patrón Service Layer
 */
@Service
@RequiredArgsConstructor
public class PrescripcionService {

    private final PrescripcionRepository prescripcionRepository;
    private final MascotaRepository mascotaRepository;
    private final HistoriaClinicaRepository historiaRepository;

    @Transactional
    public PrescripcionDTO crear(PrescripcionDTO dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada: " + dto.getMascotaId()));

        Prescripcion prescripcion = toEntity(dto);
        prescripcion.setMascota(mascota);

        // Asociar con historia clínica si existe
        if (dto.getHistoriaId() != null) {
            HistoriaClinica historia = historiaRepository.findById(dto.getHistoriaId())
                    .orElseThrow(() -> new ResourceNotFoundException("Historia clínica no encontrada: " + dto.getHistoriaId()));
            prescripcion.setHistoriaClinica(historia);
        }

        Prescripcion saved = prescripcionRepository.save(prescripcion);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PrescripcionDTO> listarTodas() {
        return prescripcionRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PrescripcionDTO> listarPorMascota(Long mascotaId) {
        return prescripcionRepository.findByMascotaIdOrderByFechaCreacionDesc(mascotaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PrescripcionDTO obtenerPorId(Long id) {
        Prescripcion prescripcion = prescripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescripción no encontrada: " + id));
        return toDTO(prescripcion);
    }

    @Transactional
    public PrescripcionDTO actualizar(Long id, PrescripcionDTO dto) {
        Prescripcion prescripcion = prescripcionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prescripción no encontrada: " + id));

        prescripcion.setMedicamento(dto.getMedicamento());
        prescripcion.setDosis(dto.getDosis());
        prescripcion.setFrecuencia(dto.getFrecuencia());
        prescripcion.setDuracionDias(dto.getDuracionDias());
        prescripcion.setIndicaciones(dto.getIndicaciones());
        prescripcion.setFechaInicio(dto.getFechaInicio());
        prescripcion.setFechaFin(dto.getFechaInicio().plusDays(dto.getDuracionDias()));

        return toDTO(prescripcionRepository.save(prescripcion));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!prescripcionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prescripción no encontrada: " + id);
        }
        prescripcionRepository.deleteById(id);
    }

    // Conversión Entity <-> DTO
    private PrescripcionDTO toDTO(Prescripcion entity) {
        PrescripcionDTO dto = new PrescripcionDTO();
        dto.setId(entity.getId());
        dto.setMedicamento(entity.getMedicamento());
        dto.setDosis(entity.getDosis());
        dto.setFrecuencia(entity.getFrecuencia());
        dto.setDuracionDias(entity.getDuracionDias());
        dto.setIndicaciones(entity.getIndicaciones());
        dto.setFechaInicio(entity.getFechaInicio());
        dto.setFechaFin(entity.getFechaFin());
        dto.setMascotaId(entity.getMascota().getId());
        dto.setMascotaNombre(entity.getMascota().getNombre());
        dto.setPropietarioNombre(
                entity.getMascota().getPropietario().getNombre() + " " +
                        entity.getMascota().getPropietario().getApellido()
        );
        if (entity.getHistoriaClinica() != null) {
            dto.setHistoriaId(entity.getHistoriaClinica().getId());
        }
        dto.setFechaCreacion(DateUtils.formatDateTime(entity.getFechaCreacion()));
        return dto;
    }

    private Prescripcion toEntity(PrescripcionDTO dto) {
        Prescripcion entity = new Prescripcion();
        entity.setMedicamento(dto.getMedicamento());
        entity.setDosis(dto.getDosis());
        entity.setFrecuencia(dto.getFrecuencia());
        entity.setDuracionDias(dto.getDuracionDias());
        entity.setIndicaciones(dto.getIndicaciones());
        entity.setFechaInicio(dto.getFechaInicio());
        entity.setFechaFin(dto.getFechaInicio().plusDays(dto.getDuracionDias()));
        return entity;
    }
}