package com.veterinaria.modules.historia.service;

import com.veterinaria.modules.historia.dto.HistoriaClinicaDTO;
import com.veterinaria.modules.historia.entity.HistoriaClinica;
import com.veterinaria.modules.historia.repository.HistoriaClinicaRepository;
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
 * Service para lógica de negocio de Historia Clínica
 * Patrón Service Layer
 */
@Service
@RequiredArgsConstructor
public class HistoriaClinicaService {

    private final HistoriaClinicaRepository historiaRepository;
    private final MascotaRepository mascotaRepository;

    @Transactional
    public HistoriaClinicaDTO crear(HistoriaClinicaDTO dto) {
        Mascota mascota = mascotaRepository.findById(dto.getMascotaId())
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada: " + dto.getMascotaId()));

        HistoriaClinica historia = toEntity(dto);
        historia.setMascota(mascota);

        HistoriaClinica saved = historiaRepository.save(historia);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> listarTodas() {
        return historiaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<HistoriaClinicaDTO> listarPorMascota(Long mascotaId) {
        return historiaRepository.findByMascotaIdOrderByFechaConsultaDesc(mascotaId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public HistoriaClinicaDTO obtenerPorId(Long id) {
        HistoriaClinica historia = historiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historia clínica no encontrada: " + id));
        return toDTO(historia);
    }

    @Transactional
    public HistoriaClinicaDTO actualizar(Long id, HistoriaClinicaDTO dto) {
        HistoriaClinica historia = historiaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Historia clínica no encontrada: " + id));

        historia.setFechaConsulta(dto.getFechaConsulta());
        historia.setDiagnostico(dto.getDiagnostico());
        historia.setSintomas(dto.getSintomas());
        historia.setTratamiento(dto.getTratamiento());
        historia.setObservaciones(dto.getObservaciones());
        historia.setPesoRegistrado(dto.getPesoRegistrado());
        historia.setTemperatura(dto.getTemperatura());

        return toDTO(historiaRepository.save(historia));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!historiaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Historia clínica no encontrada: " + id);
        }
        historiaRepository.deleteById(id);
    }

    // Conversión Entity <-> DTO
    private HistoriaClinicaDTO toDTO(HistoriaClinica entity) {
        HistoriaClinicaDTO dto = new HistoriaClinicaDTO();
        dto.setId(entity.getId());
        dto.setFechaConsulta(entity.getFechaConsulta());
        dto.setDiagnostico(entity.getDiagnostico());
        dto.setSintomas(entity.getSintomas());
        dto.setTratamiento(entity.getTratamiento());
        dto.setObservaciones(entity.getObservaciones());
        dto.setPesoRegistrado(entity.getPesoRegistrado());
        dto.setTemperatura(entity.getTemperatura());
        dto.setMascotaId(entity.getMascota().getId());
        dto.setMascotaNombre(entity.getMascota().getNombre());
        dto.setPropietarioNombre(
                entity.getMascota().getPropietario().getNombre() + " " +
                        entity.getMascota().getPropietario().getApellido()
        );
        dto.setFechaCreacion(DateUtils.formatDateTime(entity.getFechaCreacion()));
        dto.setFechaModificacion(DateUtils.formatDateTime(entity.getFechaModificacion()));
        return dto;
    }

    private HistoriaClinica toEntity(HistoriaClinicaDTO dto) {
        HistoriaClinica entity = new HistoriaClinica();
        entity.setFechaConsulta(dto.getFechaConsulta());
        entity.setDiagnostico(dto.getDiagnostico());
        entity.setSintomas(dto.getSintomas());
        entity.setTratamiento(dto.getTratamiento());
        entity.setObservaciones(dto.getObservaciones());
        entity.setPesoRegistrado(dto.getPesoRegistrado());
        entity.setTemperatura(dto.getTemperatura());
        return entity;
    }
}