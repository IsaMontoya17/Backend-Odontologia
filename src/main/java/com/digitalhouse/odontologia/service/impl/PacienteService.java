package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IPacienteRepository;
import com.digitalhouse.odontologia.service.IPacienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PacienteService implements IPacienteService {

    private static final Logger logger = LoggerFactory.getLogger(PacienteService.class);

    @Autowired
    IPacienteRepository pacienteRepository;

    @Override
    public Paciente guardar(Paciente paciente) throws HandleConflictException, BadRequestException {

        Optional<Paciente> pacienteExistente = pacienteRepository.findByDni(paciente.getDni());
        if (pacienteExistente.isPresent()) {
            logger.warn("Ya existe un paciente con el DNI: " + paciente.getDni());
            throw new HandleConflictException("Ya existe un paciente con el DNI: " + paciente.getDni());
        }

        if (paciente.getDomicilio().getCalle() == null || paciente.getDomicilio().getCalle().isEmpty() ||
                paciente.getDomicilio().getNumero() == null ||
                paciente.getDomicilio().getLocalidad() == null || paciente.getDomicilio().getLocalidad().isEmpty() ||
                paciente.getDomicilio().getProvincia() == null || paciente.getDomicilio().getProvincia().isEmpty()) {

            logger.warn("El paciente no fue guardado, todos los campos del domicilio son obligatorios.");
            throw new BadRequestException("Todos los campos del domicilio son obligatorios.");
        }
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        logger.info("Paciente guardado con ID: " + pacienteGuardado.getId());
        logger.info("Domicilio guardado con ID: " + pacienteGuardado.getDomicilio().getId());

        return pacienteGuardado;
    }

    @Override
    public Paciente buscarPorId(Long id) throws ResourceNotFoundException {
        Optional<Paciente> pacienteEncontrado = pacienteRepository.findById(id);
        if (pacienteEncontrado.isPresent()) {
            return pacienteEncontrado.get();
        } else {
            logger.warn("No se encontró el paciente con ID: " + id);
            throw new ResourceNotFoundException("No se encontró el paciente con ID: " + id);
        }
    }

    @Override
    public void eliminar(Long id) throws ResourceNotFoundException {
        logger.info("Intentando eliminar el paciente con ID: " + id);
        if (pacienteRepository.existsById(id)) {
            pacienteRepository.deleteById(id);
            logger.info("Paciente con ID: " + id + " eliminado exitosamente.");
        } else {
            logger.warn("No se puede eliminar el paciente porque no se encontró con ID: " + id);
            throw new ResourceNotFoundException("No se puede eliminar el paciente porque no se encontró con ID: " + id);
        }
    }

    @Override
    public Paciente actualizar(Paciente paciente) throws ResourceNotFoundException, BadRequestException {
        logger.info("Actualizando paciente con ID: " + paciente.getId());

        if (!pacienteRepository.existsById(paciente.getId())) {
            logger.warn("No se puede actualizar el paciente porque no se encontró el paciente con ID: " + paciente.getId());
            throw new ResourceNotFoundException("No se puede actualizar el paciente porque no se encontró el paciente con ID: " + paciente.getId());
        }

        String domicilioInfo = (paciente.getDomicilio() != null)
                ? "Domicilio ID: " + paciente.getDomicilio().getId()
                : "Domicilio no asignado";

        logger.info("Información del paciente antes de actualizar: " +
                "Paciente ID: " + paciente.getId() + ", " +
                domicilioInfo);

        Paciente pacienteActualizado = pacienteRepository.save(paciente);
        logger.info("Paciente actualizado con ID: " + pacienteActualizado.getId());

        if (pacienteActualizado.getDomicilio() != null) {
            logger.info("Domicilio del paciente actualizado con ID: " + pacienteActualizado.getDomicilio().getId());
        } else {
            logger.warn("El paciente actualizado no tiene domicilio asignado.");
            throw new BadRequestException("El paciente actualizado no tiene domicilio asignado.");
        }

        return pacienteActualizado;
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

                logger.info("Paciente ID: " + paciente.getId() + ", " + domicilioInfo);
            }
        }

        return todosLosPacientes;
    }

}