package com.veterinaria.modules.mascota.service;

import com.veterinaria.modules.mascota.dto.MascotaDTO;
import com.veterinaria.modules.mascota.entity.Mascota;
import com.veterinaria.modules.mascota.repository.MascotaRepository;
import com.veterinaria.modules.propietario.entity.Propietario;
import com.veterinaria.modules.propietario.repository.PropietarioRepository;
import com.veterinaria.shared.exception.ResourceNotFoundException;
import com.veterinaria.shared.util.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service para lógica de negocio de Mascota
 * Patrón Service Layer
 */
@Service
@RequiredArgsConstructor
public class MascotaService {

    private final MascotaRepository mascotaRepository;
    private final PropietarioRepository propietarioRepository;

    @Transactional
    public MascotaDTO crear(MascotaDTO dto) {
        // Validar que existe el propietario
        Propietario propietario = propietarioRepository.findById(dto.getPropietarioId())
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado: " + dto.getPropietarioId()));

        Mascota mascota = toEntity(dto);
        mascota.setPropietario(propietario);

        Mascota saved = mascotaRepository.save(mascota);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> listarTodas() {
        return mascotaRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MascotaDTO> listarPorPropietario(Long propietarioId) {
        return mascotaRepository.findByPropietarioId(propietarioId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MascotaDTO obtenerPorId(Long id) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada: " + id));
        return toDTO(mascota);
    }

    @Transactional
    public MascotaDTO actualizar(Long id, MascotaDTO dto) {
        Mascota mascota = mascotaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Mascota no encontrada: " + id));

        mascota.setNombre(dto.getNombre());
        mascota.setEspecie(dto.getEspecie());
        mascota.setRaza(dto.getRaza());
        mascota.setFechaNacimiento(dto.getFechaNacimiento());
        mascota.setSexo(dto.getSexo());
        mascota.setColor(dto.getColor());
        mascota.setPeso(dto.getPeso());

        return toDTO(mascotaRepository.save(mascota));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!mascotaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Mascota no encontrada: " + id);
        }
        mascotaRepository.deleteById(id);
    }

    // Conversión Entity <-> DTO
    private MascotaDTO toDTO(Mascota entity) {
        MascotaDTO dto = new MascotaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setEspecie(entity.getEspecie());
        dto.setRaza(entity.getRaza());
        dto.setFechaNacimiento(entity.getFechaNacimiento());
        dto.setSexo(entity.getSexo());
        dto.setColor(entity.getColor());
        dto.setPeso(entity.getPeso());
        dto.setPropietarioId(entity.getPropietario().getId());
        dto.setPropietarioNombre(entity.getPropietario().getNombre() + " " + entity.getPropietario().getApellido());
        dto.setPropietarioEmail(entity.getPropietario().getEmail());
        dto.setFechaRegistro(DateUtils.formatDateTime(entity.getFechaRegistro()));
        return dto;
    }

    private Mascota toEntity(MascotaDTO dto) {
        Mascota entity = new Mascota();
        entity.setNombre(dto.getNombre());
        entity.setEspecie(dto.getEspecie());
        entity.setRaza(dto.getRaza());
        entity.setFechaNacimiento(dto.getFechaNacimiento());
        entity.setSexo(dto.getSexo());
        entity.setColor(dto.getColor());
        entity.setPeso(dto.getPeso());
        return entity;
    }
}