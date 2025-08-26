package com.digitalhouse.odontologia.service;

import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;

import java.util.List;

public interface IPacienteService {
    Paciente guardar(Paciente paciente) throws HandleConflictException;

    Paciente buscarPorId(Long id)throws ResourceNotFoundException;

    void eliminar(Long id) throws ResourceNotFoundException;

    Paciente actualizar(Paciente paciente)throws ResourceNotFoundException, BadRequestException;

    List<Paciente> listar();
}
