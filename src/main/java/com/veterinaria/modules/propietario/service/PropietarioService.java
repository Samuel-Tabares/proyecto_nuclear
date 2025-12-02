package com.veterinaria.modules.propietario.service;

import com.veterinaria.modules.propietario.dto.PropietarioDTO;
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
 * Service para lógica de negocio de Propietario
 * Patrón Service Layer
 */
@Service
@RequiredArgsConstructor
public class PropietarioService {

    private final PropietarioRepository repository;

    @Transactional
    public PropietarioDTO crear(PropietarioDTO dto) {
        // Validar duplicados
        if (repository.existsByDocumento(dto.getDocumento())) {
            throw new RuntimeException("Ya existe propietario con documento: " + dto.getDocumento());
        }
        if (repository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Ya existe propietario con email: " + dto.getEmail());
        }

        Propietario propietario = toEntity(dto);
        Propietario saved = repository.save(propietario);
        return toDTO(saved);
    }

    @Transactional(readOnly = true)
    public List<PropietarioDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PropietarioDTO obtenerPorId(Long id) {
        Propietario propietario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado: " + id));
        return toDTO(propietario);
    }

    @Transactional
    public PropietarioDTO actualizar(Long id, PropietarioDTO dto) {
        Propietario propietario = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Propietario no encontrado: " + id));

        propietario.setNombre(dto.getNombre());
        propietario.setApellido(dto.getApellido());
        propietario.setTelefono(dto.getTelefono());
        propietario.setDireccion(dto.getDireccion());

        return toDTO(repository.save(propietario));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Propietario no encontrado: " + id);
        }
        repository.deleteById(id);
    }

    // Conversión Entity <-> DTO
    private PropietarioDTO toDTO(Propietario entity) {
        PropietarioDTO dto = new PropietarioDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDocumento(entity.getDocumento());
        dto.setTelefono(entity.getTelefono());
        dto.setEmail(entity.getEmail());
        dto.setDireccion(entity.getDireccion());
        dto.setFechaRegistro(DateUtils.formatDateTime(entity.getFechaRegistro()));
        return dto;
    }

    private Propietario toEntity(PropietarioDTO dto) {
        Propietario entity = new Propietario();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setDocumento(dto.getDocumento());
        entity.setTelefono(dto.getTelefono());
        entity.setEmail(dto.getEmail());
        entity.setDireccion(dto.getDireccion());
        return entity;
    }
}