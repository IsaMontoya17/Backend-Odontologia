package com.digitalhouse.odontologia.service;

import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;

import java.util.List;

public interface IOdontologoService {
    Odontologo guardar(Odontologo odontologo);
    Odontologo buscarPorId(Long id)throws ResourceNotFoundException;
    void eliminar(Long id)throws ResourceNotFoundException;
    Odontologo actualizar(Odontologo odontologo) throws ResourceNotFoundException;
    List<Odontologo> listar();
    void eliminarPorMatricula(String matricula) throws ResourceNotFoundException;
}
