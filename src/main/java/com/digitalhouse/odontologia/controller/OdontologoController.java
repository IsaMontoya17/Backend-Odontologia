package com.digitalhouse.odontologia.controller;

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

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        try {
            Odontologo odontologo = odontologoService.buscarPorId(id);
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

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminar(@PathVariable Long id) {
        try {
            odontologoService.eliminar(id);
            return ResponseEntity.status(HttpStatus.OK).body("Odontólogo eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar el odontólogo: " + e.getMessage());
        }
    }



    @PutMapping("/{id}")
    public ResponseEntity<Odontologo> actualizarOdontologo(@PathVariable Long id, @RequestBody Odontologo odontologo) {
        try {

            Odontologo odontologoExistente = odontologoService.buscarPorId(id);

            if (odontologoExistente != null) {
                odontologo.setId(id);


                Odontologo odontologoActualizado = odontologoService.actualizar(odontologo);
                return ResponseEntity.ok(odontologoActualizado);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/matricula/{matricula}")
    public ResponseEntity<String> eliminarPorMatricula(@PathVariable String matricula) {
        try {
            odontologoService.eliminarPorMatricula(matricula);
            return ResponseEntity.status(HttpStatus.OK).body("Odontólogo eliminado correctamente");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar el odontólogo: " + e.getMessage());
        }
    }
}
