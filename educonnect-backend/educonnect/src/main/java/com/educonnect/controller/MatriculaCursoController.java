package com.educonnect.controller;
 
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.educonnect.dto.CrearMatriculaDTO;
import com.educonnect.dto.MatriculaCursoDTO;
import com.educonnect.service.MatriculaCursoService;
 
@RestController 
@RequestMapping("/api/v1/matriculas") 
@CrossOrigin(origins = "http://localhost:4200") 
public class MatriculaCursoController { 
 
   private final MatriculaCursoService service; 
 
   public MatriculaCursoController( 
           MatriculaCursoService service) { 
       this.service = service; 
   } 
 
   @GetMapping 
   public ResponseEntity<List<MatriculaCursoDTO>>  
   obtenerTodas() { 
       return ResponseEntity.ok( 
           service.obtenerTodas() 
       ); 
   } 

     @GetMapping("/estado/{estado}") 
   public ResponseEntity<List<MatriculaCursoDTO>>  
   obtenerPorEstado( 
           @PathVariable String estado) { 
       return ResponseEntity.ok( 
           service.obtenerPorEstado(estado) 
       ); 
   } 
 
   @PostMapping 
   public ResponseEntity<MatriculaCursoDTO>  
   crearMatricula( 
           @RequestBody CrearMatriculaDTO dto) { 
       return ResponseEntity.status(HttpStatus.CREATED) 
           .body(service.crearMatricula(dto)); 
   } 
 
   @PatchMapping("/{id}/estado") 
   public ResponseEntity<MatriculaCursoDTO>  
   actualizarEstado( 
           @PathVariable Long id,  
           @RequestParam String nuevoEstado) { 
       return ResponseEntity.ok( 
           service.actualizarEstado(id, nuevoEstado) 
       ); 
   } 
}