package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IOdontologoRepository;
import com.digitalhouse.odontologia.service.IOdontologoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OdontologoService implements IOdontologoService {
    private static final Logger logger = LoggerFactory.getLogger(OdontologoService.class);

    @Autowired
    IOdontologoRepository odontologoRepository;

    @Override
    public Odontologo guardar(Odontologo odontologo) throws HandleConflictException {
        Optional<Odontologo> odontologoExistente = odontologoRepository.findByMatricula(odontologo.getMatricula());
        if (odontologoExistente.isPresent()) {
            logger.warn("Ya existe un odontólogo con la matrícula: " + odontologo.getMatricula());
            throw new HandleConflictException("Ya existe un odontólogo con la matrícula: " + odontologo.getMatricula());
        }
        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);
        logger.info("Odontólogo con ID: " + odontologo.getId() + " guardado exitosamente.");
        return odontologoGuardado;
    }

    @Override
    public Odontologo buscarPorId(Long id) throws ResourceNotFoundException{
        Optional<Odontologo> odontologoEncontrado = odontologoRepository.findById(id);
        if(odontologoEncontrado.isPresent()) {
            logger.info("Odontólogo con ID: " + id + " encontrado exitosamente.");
            return odontologoEncontrado.get();
        }else{
            logger.warn("No se encontro el odontologo con ID: " + id);
            throw new ResourceNotFoundException("No se encontro el odontologo con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws ResourceNotFoundException {
        if (odontologoRepository.existsById(id)) {
            odontologoRepository.deleteById(id);
            logger.info("Odontologo con ID: " + id +" eliminado exitosamente");
        } else {
            logger.warn("No se puede borrar el odontologo porque no se encontró el odontólogo con ID: " + id);
            throw new ResourceNotFoundException("No se puede borrar el odontologo porque no se encontró el odontólogo con ID: " + id);
        }
    }

    @Override
    public Odontologo actualizar(Odontologo odontologo) throws ResourceNotFoundException {
        logger.info("Actualizando odontólogo con ID: " + odontologo.getId());

        Optional<Odontologo> odontologoExistente = odontologoRepository.findById(odontologo.getId());
        if (odontologoExistente.isPresent()) {
            // Mantener el ID y actualizar otros detalles
            Odontologo odontologoActual = odontologoExistente.get();

            odontologoActual.setNombre(odontologo.getNombre());
            odontologoActual.setApellido(odontologo.getApellido());
            odontologoActual.setMatricula(odontologo.getMatricula());

            Odontologo odontologoActualizado = odontologoRepository.save(odontologoActual);
            logger.info("Odontólogo con ID: " + odontologoActualizado.getId() + " actualizado exitosamente");

            return odontologoActualizado;
        } else {
            logger.warn("No se puede modificar el odontólogo porque no se encontró el odontólogo con ID: " + odontologo.getId());
            throw new ResourceNotFoundException("No se puede modificar el odontólogo porque no se encontró el odontólogo con ID: " + odontologo.getId());
        }
    }

    @Override
    public List<Odontologo> listar() {
        List<Odontologo> todosLosOdontologos = odontologoRepository.findAll();
        if (todosLosOdontologos.isEmpty()) {
            logger.warn("No se encontraron odontólogos en la base de datos");
        }
        return todosLosOdontologos;
    }

    @Override
    public void eliminarPorMatricula(String matricula) throws ResourceNotFoundException {
        Optional<Odontologo> odontologo = odontologoRepository.findByMatricula(matricula);
        if (odontologo.isPresent()) {
            odontologoRepository.deleteByMatricula(matricula);
            logger.info("Odontólogo con matrícula: " + matricula + " eliminado exitosamente.");
        } else {
            logger.warn("No se encontró el odontólogo con matrícula: " + matricula);
            throw new ResourceNotFoundException("No se encontró el odontólogo con matrícula: " + matricula);
        }
    }

}
