package com.digitalhouse.odontologia.service;

import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;

import java.util.List;

public interface IPacienteService {
    Paciente guardar(Paciente paciente) throws HandleConflictException;
    Paciente buscarPorId(String dni)throws ResourceNotFoundException;
    void eliminar(String dni) throws ResourceNotFoundException;
    List<Paciente> listar();
}
