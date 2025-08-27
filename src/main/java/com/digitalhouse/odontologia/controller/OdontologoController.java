package com.digitalhouse.odontologia.controller;

import com.digitalhouse.odontologia.dto.OdontologoResponseDTO;
import com.digitalhouse.odontologia.dto.OdontologoUpdateDTO;
import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.service.IOdontologoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/odontologos")
public class OdontologoController {

    @Autowired
    private IOdontologoService odontologoService;

    @GetMapping("/{dni}")
    public ResponseEntity<?> buscarPorId(@PathVariable String dni) {
        try {
            Odontologo odontologo = odontologoService.buscarPorId(dni);
            return ResponseEntity.ok(odontologo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Odontologo odontologo) {
        try {
            Odontologo odontologoGuardado = odontologoService.guardar(odontologo);
            return ResponseEntity.ok(odontologoGuardado);
        } catch (HandleConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al guardar el odontólogo: " + e.getMessage());
        }
    }


    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Odontologo> listaOdontologos = odontologoService.listar();
            return ResponseEntity.ok(listaOdontologos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al listar odontólogos: " + e.getMessage());
        }
    }

    @DeleteMapping("/{dni}")
    public ResponseEntity<String> eliminar(@PathVariable String dni) {
        try {
            odontologoService.eliminar(dni);
            return ResponseEntity.status(HttpStatus.OK).body("Odontólogo eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar el odontólogo: " + e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            Odontologo odontologo = odontologoService.buscarPorEmail(email);
            return ResponseEntity.ok(odontologo);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al buscar el odontólogo: " + e.getMessage());
        }
    }

    @GetMapping("/especialidad/{especialidad}")
    public ResponseEntity<?> buscarPorEspecialidad(@PathVariable String especialidad) {
        try {
            List<Odontologo> odontologos = odontologoService.buscarPorEspecialidad(especialidad);
            return ResponseEntity.ok(odontologos);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al buscar odontólogos por especialidad: " + e.getMessage());
        }
    }

    @PatchMapping("/{dni}")
    public ResponseEntity<OdontologoResponseDTO> actualizarOdontologo(
            @PathVariable String dni,
            @RequestBody OdontologoUpdateDTO dto) {

        OdontologoResponseDTO actualizado = odontologoService.actualizarOdontologo(dni, dto);
        return ResponseEntity.ok(actualizado);
    }

}
