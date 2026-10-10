package com.educonnect.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.educonnect.model.MatriculaCurso;

@Repository
public interface MatriculaCursoRepository extends JpaRepository<MatriculaCurso, Long> {

    List<MatriculaCurso>
            findByEstadoOrderByFechaMatriculaDesc(
                    String estado
            );

    List<MatriculaCurso>
            findAllByOrderByFechaMatriculaDesc();
}
