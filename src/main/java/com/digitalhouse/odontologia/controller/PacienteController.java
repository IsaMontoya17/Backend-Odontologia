package com.digitalhouse.odontologia.controller;

import com.digitalhouse.odontologia.dto.PacienteResponseDTO;
import com.digitalhouse.odontologia.dto.PacienteUpdateDTO;
import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.service.IPacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pacientes")
public class PacienteController {

    @Autowired
    private IPacienteService pacienteService;

    @GetMapping("/{dni}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PACIENTE') and #dni == authentication.principal.dni)")
    public ResponseEntity<?> buscarPorId(@PathVariable String dni) {
        try {
            Paciente paciente = pacienteService.buscarPorId(dni);
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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> listarTodos() {
        try {
            List<Paciente> listaPacientes = pacienteService.listar();
            return ResponseEntity.ok(listaPacientes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al listar pacientes: " + e.getMessage());
        }
    }

    @DeleteMapping("/{dni}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> eliminar(@PathVariable String dni) {
        try {
            pacienteService.eliminar(dni);
            return ResponseEntity.status(HttpStatus.OK).body("Paciente eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar el paciente: " + e.getMessage());
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> buscarPorEmail(@PathVariable String email) {
        try {
            Paciente paciente = pacienteService.buscarPorEmail(email);
            return ResponseEntity.ok(paciente);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error interno al buscar el paciente: " + e.getMessage());
        }
    }

    @PatchMapping("/{dni}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('PACIENTE') and #dni == authentication.principal.dni)")
    public ResponseEntity<PacienteResponseDTO> actualizarPaciente(
            @PathVariable String dni,
            @RequestBody PacienteUpdateDTO dto) {

        try {
            PacienteResponseDTO actualizado = pacienteService.actualizarPaciente(dni, dto);
            return ResponseEntity.ok(actualizado);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
