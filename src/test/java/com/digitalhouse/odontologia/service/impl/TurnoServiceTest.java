package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.entity.Domicilio;
import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.entity.Turno;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IOdontologoRepository;
import com.digitalhouse.odontologia.repository.IPacienteRepository;
import com.digitalhouse.odontologia.repository.ITurnoRepository;
import com.digitalhouse.odontologia.service.ITurnoService;
import org.apache.coyote.BadRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TurnoServiceTest {

    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IOdontologoRepository odontologoRepository;

    @Autowired
    private IPacienteRepository pacienteRepository;

    @Autowired
    private ITurnoService turnoService;

    @BeforeEach
    void setUp() {
        // Limpiar la base de datos antes de cada test
        turnoRepository.deleteAll();
        pacienteRepository.deleteAll();
        odontologoRepository.deleteAll();
    }

    @Test
    void testRegistrarTurno() throws BadRequestException, HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Juan");
        odontologo.setApellido("Pérez");
        odontologo.setMatricula("12345");
        odontologoRepository.save(odontologo);

        // Crear y guardar un paciente
        Paciente paciente = new Paciente();
        paciente.setNombre("Ana");
        paciente.setApellido("Gómez");
        paciente.setDni("67890");
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle 1");
        domicilio.setNumero(10);
        domicilio.setLocalidad("Localidad");
        domicilio.setProvincia("Provincia");
        paciente.setDomicilio(domicilio);
        pacienteRepository.save(paciente);

        // Registrar un turno
        Turno turno = turnoService.registrarTurno(odontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0));

        // Verificar que el turno fue registrado correctamente
        assertNotNull(turno.getId());
        assertEquals(odontologo.getId(), turno.getOdontologo().getId());
        assertEquals(paciente.getId(), turno.getPaciente().getId());
        assertEquals(LocalDate.now(), turno.getFecha());
        assertEquals(LocalTime.of(10, 0), turno.getHora());
    }

    @Test
    void testRegistrarTurnoOdontologoConflicto() throws BadRequestException, HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Juan");
        odontologo.setApellido("Pérez");
        odontologo.setMatricula("12345");
        odontologoRepository.save(odontologo);

        // Crear y guardar un paciente
        Paciente paciente = new Paciente();
        paciente.setNombre("Ana");
        paciente.setApellido("Gómez");
        paciente.setDni("67890");
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle 1");
        domicilio.setNumero(10);
        domicilio.setLocalidad("Localidad");
        domicilio.setProvincia("Provincia");
        paciente.setDomicilio(domicilio);
        pacienteRepository.save(paciente);

        // Registrar un primer turno
        turnoService.registrarTurno(odontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0));

        // Intentar registrar un segundo turno para el mismo odontólogo en la misma fecha y hora
        assertThrows(HandleConflictException.class, () ->
                turnoService.registrarTurno(odontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0)));
    }

    @Test
    void testRegistrarTurnoPacienteConflicto() throws BadRequestException, HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Juan");
        odontologo.setApellido("Pérez");
        odontologo.setMatricula("12345");
        odontologoRepository.save(odontologo);

        // Crear y guardar un paciente
        Paciente paciente = new Paciente();
        paciente.setNombre("Ana");
        paciente.setApellido("Gómez");
        paciente.setDni("67890");
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle 1");
        domicilio.setNumero(10);
        domicilio.setLocalidad("Localidad");
        domicilio.setProvincia("Provincia");
        paciente.setDomicilio(domicilio);
        pacienteRepository.save(paciente);

        // Registrar un primer turno
        turnoService.registrarTurno(odontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0));

        // Intentar registrar un segundo turno para el mismo paciente en la misma fecha y hora
        Odontologo otroOdontologo = new Odontologo();
        otroOdontologo.setNombre("María");
        otroOdontologo.setApellido("López");
        otroOdontologo.setMatricula("67890");
        odontologoRepository.save(otroOdontologo);

        assertThrows(HandleConflictException.class, () ->
                turnoService.registrarTurno(otroOdontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0)));
    }

    @Test
    void testObtenerTodosLosTurnos() throws BadRequestException, HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Juan");
        odontologo.setApellido("Pérez");
        odontologo.setMatricula("12345");
        odontologoRepository.save(odontologo);

        // Crear y guardar un paciente
        Paciente paciente = new Paciente();
        paciente.setNombre("Ana");
        paciente.setApellido("Gómez");
        paciente.setDni("67890");
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle 1");
        domicilio.setNumero(10);
        domicilio.setLocalidad("Localidad");
        domicilio.setProvincia("Provincia");
        paciente.setDomicilio(domicilio);
        pacienteRepository.save(paciente);

        // Registrar un turno
        turnoService.registrarTurno(odontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0));

        // Obtener todos los turnos
        List<Turno> turnos = turnoService.obtenerTodosLosTurnos();

        // Verificar que la lista de turnos no es nula y contiene el turno registrado
        assertNotNull(turnos);
        assertEquals(1, turnos.size());
    }

    @Test
    void testEliminarTurno() throws BadRequestException, HandleConflictException, ResourceNotFoundException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Juan");
        odontologo.setApellido("Pérez");
        odontologo.setMatricula("12345");
        odontologoRepository.save(odontologo);

        // Crear y guardar un paciente
        Paciente paciente = new Paciente();
        paciente.setNombre("Ana");
        paciente.setApellido("Gómez");
        paciente.setDni("67890");
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle 1");
        domicilio.setNumero(10);
        domicilio.setLocalidad("Localidad");
        domicilio.setProvincia("Provincia");
        paciente.setDomicilio(domicilio);
        pacienteRepository.save(paciente);
        Turno turno = turnoService.registrarTurno(odontologo.getId(), paciente.getId(), LocalDate.now(), LocalTime.of(10, 0));
        turnoService.eliminarTurno(turno.getId());

        // Verificar que el turno ha sido eliminado
        assertThrows(ResourceNotFoundException.class, () ->
                turnoService.eliminarTurno(turno.getId()));
    }
}