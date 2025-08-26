package com.digitalhouse.odontologia.service;

import com.digitalhouse.odontologia.dto.OdontologoResponseDTO;
import com.digitalhouse.odontologia.dto.OdontologoUpdateDTO;
import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;

import java.util.List;

public interface IOdontologoService {
    Odontologo guardar(Odontologo odontologo);
    Odontologo buscarPorId(String dni) throws ResourceNotFoundException;
    void eliminar(String dni) throws ResourceNotFoundException;
    List<Odontologo> listar();

    OdontologoResponseDTO actualizarOdontologo(String dni, OdontologoUpdateDTO dto);
}
