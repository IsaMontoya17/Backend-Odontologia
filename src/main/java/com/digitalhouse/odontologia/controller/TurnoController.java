package com.digitalhouse.odontologia.controller;

import com.digitalhouse.odontologia.entity.Turno;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.service.impl.TurnoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/turnos")
public class TurnoController {
    @Autowired
    private TurnoService turnoService;

    @PostMapping
    public ResponseEntity<?> registrarTurno(@RequestBody Map<String, Object> request) {
        try {
            Long odontologoId = Long.valueOf(request.get("odontologoId").toString());
            Long pacienteId = Long.valueOf(request.get("pacienteId").toString());
            LocalDate fecha = LocalDate.parse(request.get("fecha").toString());
            LocalTime hora = LocalTime.parse(request.get("hora").toString());

            Turno nuevoTurno = turnoService.registrarTurno(odontologoId, pacienteId, fecha, hora);
            return ResponseEntity.ok(nuevoTurno);
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch(HandleConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al registrar el turno: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Turno>> obtenerTurnos() {
        List<Turno> turnos = turnoService.obtenerTodosLosTurnos();
        return ResponseEntity.ok(turnos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            turnoService.eliminarTurno(id);
            return ResponseEntity.status(HttpStatus.OK).body("Turno eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar el turno: " + e.getMessage());
        }
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<?> buscarTurnosPorFecha(@PathVariable String fecha) {
        try {
            LocalDate fechaParseada = LocalDate.parse(fecha);
            List<Turno> turnos = turnoService.buscarTurnosPorFecha(fechaParseada);
            return ResponseEntity.ok(turnos);
        }catch (ResourceNotFoundException e) {
            return ResponseEntity.ok(Collections.emptyList());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al buscar turnoa: " + e.getMessage());
        }
    }

}
