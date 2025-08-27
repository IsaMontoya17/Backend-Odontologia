package com.digitalhouse.odontologia.service;

import com.digitalhouse.odontologia.dto.PacienteResponseDTO;
import com.digitalhouse.odontologia.dto.PacienteUpdateDTO;
import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;

import java.util.List;

public interface IPacienteService {
    Paciente guardar(Paciente paciente) throws HandleConflictException;
    Paciente buscarPorId(String dni)throws ResourceNotFoundException;
    void eliminar(String dni) throws ResourceNotFoundException;
    List<Paciente> listar();
    Paciente buscarPorEmail(String email) throws ResourceNotFoundException;
    PacienteResponseDTO actualizarPaciente(String dni, PacienteUpdateDTO dto) throws ResourceNotFoundException;
}
