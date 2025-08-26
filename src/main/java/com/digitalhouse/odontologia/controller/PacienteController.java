package com.digitalhouse.odontologia.controller;

import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private IPacienteService pacienteService;

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Paciente paciente = pacienteService.buscarPorId(id);
            return ResponseEntity.ok(paciente);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Paciente paciente) {
        try {
            Paciente pacienteGuardado = pacienteService.guardar(paciente);
            return ResponseEntity.ok(pacienteGuardado);
        } catch (HandleConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }catch (BadRequestException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al guardar el paciente: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listarTodos() {
        try {
            List<Paciente> listaPacientes = pacienteService.listar();
            return ResponseEntity.ok(listaPacientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al listar pacientes: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            pacienteService.eliminar(id);
            return ResponseEntity.status(HttpStatus.OK).body("Paciente eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar el paciente: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @RequestBody Paciente paciente) {
        try {
            Paciente pacienteExistente = pacienteService.buscarPorId(id);

            if (pacienteExistente != null) {
                // El ID del paciente se mantiene igual
                paciente.setId(id);

                // El ID del domicilio se mantiene igual
                if (pacienteExistente.getDomicilio() != null) {
                    paciente.getDomicilio().setId(pacienteExistente.getDomicilio().getId());
                }

                Paciente pacienteActualizado = pacienteService.actualizar(paciente);
                return ResponseEntity.ok(pacienteActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (BadRequestException e){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al actualizar el paciente: " + e.getMessage());
        }
    }

}
