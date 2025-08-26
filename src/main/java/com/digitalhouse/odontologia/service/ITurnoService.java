package com.digitalhouse.odontologia.service;

import com.digitalhouse.odontologia.entity.Turno;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import org.apache.coyote.BadRequestException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ITurnoService {
    Turno registrarTurno (Long odontologoId, Long pacienteId, LocalDate fecha, LocalTime hora)throws BadRequestException, HandleConflictException;
    public List<Turno> obtenerTodosLosTurnos();
    void eliminarTurno(Long turnoId) throws ResourceNotFoundException;

    List<Turno> buscarTurnosPorFecha(LocalDate fecha) throws ResourceNotFoundException;

}
