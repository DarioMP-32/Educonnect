package com.educonnect.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.educonnect.dto.CrearMatriculaDTO;
import com.educonnect.dto.MatriculaCursoDTO;
import com.educonnect.model.Estudiante;
import com.educonnect.model.MatriculaCurso;
import com.educonnect.model.Profesor;
import com.educonnect.repository.EstudianteRepository;
import com.educonnect.repository.MatriculaCursoRepository;
import com.educonnect.repository.ProfesorRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class MatriculaCursoService {

    private static final String ESTADO_INICIAL = "ACTIVA";
    private static final Set<String> ESTADOS_VALIDOS = Set.of("ACTIVA", "COMPLETADA", "CANCELADA");

    private final MatriculaCursoRepository matriculaRepository;
    private final EstudianteRepository estudianteRepository;
    private final ProfesorRepository profesorRepository;

    public MatriculaCursoService(MatriculaCursoRepository matriculaRepository,
            EstudianteRepository estudianteRepository,
            ProfesorRepository profesorRepository) {
        this.matriculaRepository = matriculaRepository;
        this.estudianteRepository = estudianteRepository;
        this.profesorRepository = profesorRepository;
    }

    // 1. Todas las matriculas, la mas reciente primero
    @Transactional(readOnly = true)
    public List<MatriculaCursoDTO> obtenerTodas() {
        return matriculaRepository.findAllByOrderByFechaMatriculaDesc().stream().map(this::convertirADTO).toList();
    }

    // 2. Matriculas filtradas por estado (ACTIVA, COMPLETADA, CANCELADA)
    @Transactional(readOnly = true)
    public List<MatriculaCursoDTO> obtenerPorEstado(String estado) {
        String estadoNormalizado = validarEstado(estado);
        return matriculaRepository.findByEstadoOrderByFechaMatriculaDesc(estadoNormalizado).stream().map(this::convertirADTO).toList();
    }

    // 3. Crear una matricula nueva
    @Transactional
    public MatriculaCursoDTO crearMatricula(CrearMatriculaDTO dto) {
        if (dto.getEstudianteId() == null || dto.getProfesorId() == null) {
            throw new IllegalArgumentException("El estudiante y el profesor son obligatorios.");
        }

        Estudiante estudiante = estudianteRepository.findById(dto.getEstudianteId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Estudiante no encontrado con id " + dto.getEstudianteId()));

        Profesor profesor = profesorRepository.findById(dto.getProfesorId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Profesor no encontrado con id " + dto.getProfesorId()));

        if (!profesor.isActivo()) {
            throw new IllegalArgumentException("El profesor seleccionado no se encuentra activo.");
        }

        MatriculaCurso matricula = new MatriculaCurso();
        matricula.setCodigoMatricula("MAT-2026-" + System.currentTimeMillis());
        matricula.setEstudiante(estudiante);
        matricula.setProfesor(profesor);
        matricula.setNombreCurso(dto.getNombreCurso());
        matricula.setCreditos(dto.getCreditos());
        matricula.setPeriodo(dto.getPeriodo());
        matricula.setMontoArancel(dto.getMontoArancel());
        matricula.setEstado(ESTADO_INICIAL);
        matricula.setFechaMatricula(LocalDateTime.now());

        return convertirADTO(matriculaRepository.save(matricula));
    }

    // 4. Cambiar el estado de una matricula existente
    @Transactional
    public MatriculaCursoDTO actualizarEstado(Long id, String nuevoEstado) {
        String estadoNormalizado = validarEstado(nuevoEstado);

        MatriculaCurso matricula = matriculaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Matricula no encontrada con id " + id));

        matricula.setEstado(estadoNormalizado);
        return convertirADTO(matriculaRepository.save(matricula));
    }

    // Entidad -> DTO (aplana estudiante y profesor)
    private MatriculaCursoDTO convertirADTO(MatriculaCurso m) {
        MatriculaCursoDTO dto = new MatriculaCursoDTO();
        dto.setId(m.getId());
        dto.setCodigoMatricula(m.getCodigoMatricula());
        dto.setNombreEstudiante(m.getEstudiante().getNombreCompleto());
        dto.setCarnetEstudiante(m.getEstudiante().getCarnet());
        dto.setNombreProfesor(m.getProfesor().getNombre());
        dto.setNombreCurso(m.getNombreCurso());
        dto.setCreditos(m.getCreditos());
        dto.setPeriodo(m.getPeriodo());
        dto.setEstado(m.getEstado());
        dto.setMontoArancel(m.getMontoArancel());
        dto.setFechaMatricula(m.getFechaMatricula());
        return dto;
    }

    private String validarEstado(String estado) {
        if (estado == null || !ESTADOS_VALIDOS.contains(estado.trim().toUpperCase())) {
            throw new IllegalArgumentException(
                    "Estado invalido. Valores permitidos: ACTIVA, COMPLETADA, CANCELADA.");
        }
        return estado.trim().toUpperCase();
    }
}
