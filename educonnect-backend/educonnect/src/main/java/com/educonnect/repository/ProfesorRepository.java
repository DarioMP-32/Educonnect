package com.educonnect.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.educonnect.model.Profesor;


public interface ProfesorRepository extends JpaRepository<Profesor, Long> {
    
}
