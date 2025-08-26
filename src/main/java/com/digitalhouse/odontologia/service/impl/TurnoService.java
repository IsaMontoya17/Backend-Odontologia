package com.digitalhouse.odontologia.service.impl;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TurnoService implements ITurnoService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);
    @Autowired
    private ITurnoRepository turnoRepository;

    @Autowired
    private IOdontologoRepository odontologoRepository;

    @Autowired
    private IPacienteRepository pacienteRepository;

    public Turno registrarTurno(String odontologoId, Long pacienteId, LocalDate fecha, LocalTime hora) throws BadRequestException, HandleConflictException {

        Optional<Odontologo> odontologoOptional = odontologoRepository.findById(odontologoId);
        if (odontologoOptional.isEmpty()) {
            logger.warn("No se puede realizar la solicitud porque no se ha encontrado un odontologo con ID: " + odontologoId);
            throw new BadRequestException("No se puede realizar la solicitud porque no se ha encontrado un odontologo con ID: " + odontologoId);
        }

        Optional<Paciente> pacienteOptional = pacienteRepository.findById(pacienteId);
        if (pacienteOptional.isEmpty()) {
            logger.warn("No se puede realizar la solicitud porque no se ha encontrado un paciente con ID: " + pacienteId);
            throw new BadRequestException("No se puede realizar la solicitud porque no se ha encontrado un paciente con ID: " + pacienteId);
        }


        boolean existeTurnoOdontologo = turnoRepository.findAll().stream()
                .anyMatch(turno -> turno.getOdontologo().getDni().equals(odontologoId)
                        && turno.getFecha().equals(fecha)
                        && turno.getHora().equals(hora));

        if (existeTurnoOdontologo) {
            logger.warn("Turno no disponible. El odontólogo ya tiene un turno asignado en esa fecha y hora.");
            throw new HandleConflictException("Turno no disponible. El odontólogo ya tiene un turno asignado en esa fecha y hora.");
        }


        boolean existeTurnoPaciente = turnoRepository.findAll().stream()
                .anyMatch(turno -> turno.getPaciente().getId().equals(pacienteId)
                        && turno.getFecha().equals(fecha)
                        && turno.getHora().equals(hora));

        if (existeTurnoPaciente) {
            logger.warn("Turno no disponible. El paciente ya tiene un turno asignado en esa fecha y hora.");
            throw new HandleConflictException("Turno no disponible. El paciente ya tiene un turno asignado en esa fecha y hora.");
        }

        Turno nuevoTurno = new Turno();
        nuevoTurno.setOdontologo(odontologoOptional.get());
        nuevoTurno.setPaciente(pacienteOptional.get());
        nuevoTurno.setFecha(fecha);
        nuevoTurno.setHora(hora);

        return turnoRepository.save(nuevoTurno);
    }

    public List<Turno> obtenerTodosLosTurnos() {
        List<Turno> turnos = turnoRepository.findAll();
        if (turnos.isEmpty()) {
            logger.warn("No se encontraron turnos en la base de datos.");
        }
        return turnos;
    }

    @Override
    public void eliminarTurno(Long turnoId) throws ResourceNotFoundException{
        if (turnoRepository.existsById(turnoId)) {
            turnoRepository.deleteById(turnoId);
        } else {
            logger.warn("Turno no encontrado con ID: " + turnoId);
            throw new ResourceNotFoundException("Turno no encontrado con ID: " + turnoId);
        }
    }

    @Override
    public List<Turno> buscarTurnosPorFecha(LocalDate fecha) throws ResourceNotFoundException {
        List<Turno> turnos = turnoRepository.findByFecha(fecha);
        if (turnos.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron turnos para la fecha: " + fecha);
        }
        return turnos;
    }

}
