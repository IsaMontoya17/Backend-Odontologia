package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.dto.OdontologoResponseDTO;
import com.digitalhouse.odontologia.dto.OdontologoUpdateDTO;
import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.entity.User;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IOdontologoRepository;
import com.digitalhouse.odontologia.repository.IUserRepository;
import com.digitalhouse.odontologia.service.IOdontologoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OdontologoService implements IOdontologoService {
    private static final Logger logger = LoggerFactory.getLogger(OdontologoService.class);

    @Autowired
    IOdontologoRepository odontologoRepository;

    @Autowired
    IUserRepository userRepository;

    @Override
    public Odontologo guardar(Odontologo odontologo) throws HandleConflictException {
        if (odontologoRepository.existsById(odontologo.getDni())) {
            logger.warn("Ya existe un odontólogo con el DNI: " + odontologo.getDni());
            throw new HandleConflictException("Ya existe un odontólogo con el DNI: " + odontologo.getDni());
        }

        Odontologo odontologoGuardado = odontologoRepository.save(odontologo);
        logger.info("Odontólogo con DNI: " + odontologoGuardado.getDni() + " guardado exitosamente.");
        return odontologoGuardado;
    }

    @Override
    public Odontologo buscarPorId(String dni) throws ResourceNotFoundException{
        Optional<Odontologo> odontologoEncontrado = odontologoRepository.findById(dni);
        if(odontologoEncontrado.isPresent()) {
            logger.info("Odontólogo con ID: " + dni + " encontrado exitosamente.");
            return odontologoEncontrado.get();
        }else{
            logger.warn("No se encontro el odontologo con ID: " + dni);
            throw new ResourceNotFoundException("No se encontro el odontologo con ID: " + dni);
        }
    }

    @Override
    public void eliminar(String dni) throws ResourceNotFoundException {
        if (odontologoRepository.existsById(dni)) {
            odontologoRepository.deleteById(dni);
            logger.info("Odontologo con ID: " + dni +" eliminado exitosamente");
        } else {
            logger.warn("No se puede borrar el odontologo porque no se encontró el odontólogo con ID: " + dni);
            throw new ResourceNotFoundException("No se puede borrar el odontologo porque no se encontró el odontólogo con ID: " + dni);
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
    public Odontologo buscarPorEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Odontologo no encontrado con email: " + email));
        if (user instanceof Odontologo odontologo) {
            return odontologo;
        } else {
            throw new ResourceNotFoundException("El usuario con email " + email + " no es un odontólogo");
        }
    }

    @Override
    public OdontologoResponseDTO actualizarOdontologo(String dni, OdontologoUpdateDTO dto) throws ResourceNotFoundException{
        Odontologo odontologo = odontologoRepository.findById(dni)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));

        if (dto.getApellido() != null) odontologo.setApellido(dto.getApellido());
        if (dto.getNombre() != null) odontologo.setNombre(dto.getNombre());
        if (dto.getEmail() != null) odontologo.setEmail(dto.getEmail());
        if (dto.getTelefono() != null) odontologo.setTelefono(dto.getTelefono());
        if (dto.getEspecialidad() != null) odontologo.setEspecialidad(dto.getEspecialidad());

        Odontologo actualizado = odontologoRepository.save(odontologo);

        OdontologoResponseDTO responseDTO = new OdontologoResponseDTO();
        responseDTO.setDni(actualizado.getDni());
        responseDTO.setApellido(actualizado.getApellido());
        responseDTO.setNombre(actualizado.getNombre());
        responseDTO.setEmail(actualizado.getEmail());
        responseDTO.setTelefono(actualizado.getTelefono());
        responseDTO.setEspecialidad(actualizado.getEspecialidad());
        responseDTO.setTurnosIds(actualizado.getTurnos()
                .stream()
                .map(t -> t.getId())
                .collect(Collectors.toSet()));

        return responseDTO;
    }


}
