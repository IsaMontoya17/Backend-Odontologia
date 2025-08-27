package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.dto.DomicilioUpdateDTO;
import com.digitalhouse.odontologia.dto.PacienteResponseDTO;
import com.digitalhouse.odontologia.dto.PacienteUpdateDTO;
import com.digitalhouse.odontologia.entity.*;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IPacienteRepository;
import com.digitalhouse.odontologia.repository.IUserRepository;
import com.digitalhouse.odontologia.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PacienteService implements IPacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Autowired
    IPacienteRepository pacienteRepository;

    @Autowired
    IUserRepository userRepository;

    @Override
    public Paciente guardar(Paciente paciente) throws HandleConflictException, BadRequestException {

        if (pacienteRepository.existsById(paciente.getDni())) {
            logger.warn("Ya existe un paciente con el DNI: " + paciente.getDni());
            throw new HandleConflictException("Ya existe un paciente con el DNI: " + paciente.getDni());
        }

        if (paciente.getNombre() == null || paciente.getNombre().isEmpty()
                || paciente.getApellido() == null || paciente.getApellido().isEmpty()
                || paciente.getFechaNacimiento() == null
                || paciente.getDomicilio() == null
                || paciente.getDomicilio().getCalle() == null || paciente.getDomicilio().getCalle().isEmpty()
                || paciente.getDomicilio().getNumero() == null || paciente.getDomicilio().getNumero().isEmpty()
                || paciente.getDomicilio().getBarrio() == null || paciente.getDomicilio().getBarrio().isEmpty()
                || paciente.getDomicilio().getCiudad() == null || paciente.getDomicilio().getCiudad().isEmpty()
                || paciente.getDomicilio().getDepartamento() == null || paciente.getDomicilio().getDepartamento().isEmpty()) {

            logger.warn("Todos los campos del paciente y del domicilio son obligatorios.");
            throw new BadRequestException("Todos los campos del paciente y del domicilio son obligatorios.");
        }

        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        logger.info("Paciente guardado con DNI: " + pacienteGuardado.getDni());
        logger.info("Domicilio guardado con ID: " + pacienteGuardado.getDomicilio().getId());

        return pacienteGuardado;
    }


    @Override
    public Paciente buscarPorId(String dni) throws ResourceNotFoundException {
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findById(dni);
        if (pacienteEncontrado.isPresent()) {
            logger.info("Paciente con ID: " + dni + " encontrado exitosamente.");
            return pacienteEncontrado.get();
        } else {
            logger.warn("No se encontró el paciente con ID: " + dni);
            throw new ResourceNotFoundException("No se encontró el paciente con ID: " + dni);
        }
    }

    @Override
    public void eliminar(String dni) throws ResourceNotFoundException {
        logger.info("Intentando eliminar el paciente con ID: " + dni);
        if (pacienteRepository.existsById(dni)) {
            pacienteRepository.deleteById(dni);
            logger.info("Paciente con ID: " + dni + " eliminado exitosamente.");
        } else {
            logger.warn("No se puede eliminar el paciente porque no se encontró con ID: " + dni);
            throw new ResourceNotFoundException("No se puede eliminar el paciente porque no se encontró con ID: " + dni);
        }
    }

    @Override
    public List<Paciente> listar() {
        List<Paciente> todosLosPacientes = pacienteRepository.findAll();

        if (todosLosPacientes.isEmpty()) {
            logger.info("No se encontraron pacientes en la base de datos.");
        } else {
            logger.info("Pacientes: ");
            for (Paciente paciente : todosLosPacientes) {
                String domicilioInfo = (paciente.getDomicilio() != null)
                        ? "Domicilio ID: " + paciente.getDomicilio().getId()
                        : "Domicilio no asignado";

                logger.info("Paciente ID: " + paciente.getDni() + ", " + domicilioInfo);
            }
        }

        return todosLosPacientes;
    }

    @Override
    public Paciente buscarPorEmail(String email) throws ResourceNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con email: " + email));
        if (user instanceof Paciente paciente) {
            return paciente;
        } else {
            throw new ResourceNotFoundException("El usuario con email " + email + " no es un paciente");
        }
    }

    @Override
    public PacienteResponseDTO actualizarPaciente(String dni, PacienteUpdateDTO dto) throws ResourceNotFoundException {
        Paciente paciente = pacienteRepository.findById(dni)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        if (dto.getNombre() != null) paciente.setNombre(dto.getNombre());
        if (dto.getApellido() != null) paciente.setApellido(dto.getApellido());
        if (dto.getFechaNacimiento() != null) paciente.setFechaNacimiento(dto.getFechaNacimiento());

        if (dto.getDomicilio() != null) {
            if (paciente.getDomicilio() == null) paciente.setDomicilio(new Domicilio());
            Domicilio domicilio = paciente.getDomicilio();
            DomicilioUpdateDTO dDto = dto.getDomicilio();
            if (dDto.getCalle() != null) domicilio.setCalle(dDto.getCalle());
            if (dDto.getNumero() != null) domicilio.setNumero(dDto.getNumero());
            if (dDto.getBarrio() != null) domicilio.setBarrio(dDto.getBarrio());
            if (dDto.getCiudad() != null) domicilio.setCiudad(dDto.getCiudad());
            if (dDto.getDepartamento() != null) domicilio.setDepartamento(dDto.getDepartamento());
        }

        Paciente actualizado = pacienteRepository.save(paciente);

        PacienteResponseDTO responseDTO = new PacienteResponseDTO();
        responseDTO.setDni(actualizado.getDni());
        responseDTO.setNombre(actualizado.getNombre());
        responseDTO.setApellido(actualizado.getApellido());
        responseDTO.setFechaNacimiento(actualizado.getFechaNacimiento());

        if (actualizado.getDomicilio() != null) {
            var domicilioResponse = new com.digitalhouse.odontologia.dto.DomicilioResponseDTO();
            domicilioResponse.setCalle(actualizado.getDomicilio().getCalle());
            domicilioResponse.setNumero(actualizado.getDomicilio().getNumero());
            domicilioResponse.setBarrio(actualizado.getDomicilio().getBarrio());
            domicilioResponse.setCiudad(actualizado.getDomicilio().getCiudad());
            domicilioResponse.setDepartamento(actualizado.getDomicilio().getDepartamento());
            responseDTO.setDomicilio(domicilioResponse);
        }

        responseDTO.setTurnosIds(actualizado.getTurnos()
                .stream()
                .map(Turno::getId)
                .collect(Collectors.toSet()));

        return responseDTO;
    }

}