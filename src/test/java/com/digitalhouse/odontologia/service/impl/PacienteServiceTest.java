package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.entity.Domicilio;
import com.digitalhouse.odontologia.entity.Paciente;
import com.digitalhouse.odontologia.exception.BadRequestException;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IPacienteRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PacienteServiceTest {

    @Autowired
    private IPacienteRepository pacienteRepository;

    @Autowired
    private PacienteService pacienteService;

    @Test
    void testGuardarPaciente() throws HandleConflictException, BadRequestException {
        // Crear un nuevo paciente con domicilio
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle Falsa");
        domicilio.setLocalidad("Ciudad");
        domicilio.setNumero(123);
        domicilio.setProvincia("Provincia");

        Paciente paciente = new Paciente();
        paciente.setNombre("Juan");
        paciente.setApellido("Pérez");
        paciente.setDni("12345678");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(domicilio);

        // Guardar el paciente
        Paciente guardado = pacienteService.guardar(paciente);

        // Verificar que el paciente fue guardado correctamente
        assertNotNull(guardado.getId());
        assertEquals("Juan", guardado.getNombre());
        assertEquals("Pérez", guardado.getApellido());
        assertEquals("12345678", guardado.getDni());
        assertNotNull(guardado.getDomicilio().getId());
    }

    @Test
    void testGuardarPacienteConDniDuplicado() throws HandleConflictException, BadRequestException {
        // Crear y guardar un paciente
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle Original");
        domicilio.setLocalidad("Ciudad");
        domicilio.setNumero(321);
        domicilio.setProvincia("Provincia");

        Paciente paciente1 = new Paciente();
        paciente1.setNombre("Pedro");
        paciente1.setApellido("Gómez");
        paciente1.setDni("99999999");
        paciente1.setFechaAlta(LocalDate.now());
        paciente1.setDomicilio(domicilio);
        pacienteService.guardar(paciente1);

        // Intentar guardar otro paciente con el mismo dni
        Paciente paciente2 = new Paciente();
        paciente2.setNombre("Ana");
        paciente2.setApellido("Martínez");
        paciente2.setDni("99999999");
        paciente2.setFechaAlta(LocalDate.now());
        paciente2.setDomicilio(domicilio);

        assertThrows(HandleConflictException.class, () -> pacienteService.guardar(paciente2));
    }

    @Test
    void testBuscarPorId() throws HandleConflictException, BadRequestException, ResourceNotFoundException {
        // Crear y guardar un paciente
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle Verdadera");
        domicilio.setLocalidad("Ciudad");
        domicilio.setNumero(456);
        domicilio.setProvincia("Provincia");

        Paciente paciente = new Paciente();
        paciente.setNombre("Ana");
        paciente.setApellido("Gómez");
        paciente.setDni("87654321");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(domicilio);

        Paciente guardado = pacienteService.guardar(paciente);

        // Buscar por id
        Paciente encontrado = pacienteService.buscarPorId(guardado.getId());

        // Verificar que el paciente fue encontrado correctamente
        assertNotNull(encontrado);
        assertEquals(guardado.getId(), encontrado.getId());
        assertEquals("Ana", encontrado.getNombre());
        assertEquals("Gómez", encontrado.getApellido());
        assertEquals("87654321", encontrado.getDni());
        assertEquals(domicilio.getId(), encontrado.getDomicilio().getId());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        // Intentar buscar un paciente con un ID no existente
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.buscarPorId(999L));
    }

    @Test
    void testEliminarPaciente() throws HandleConflictException, BadRequestException, ResourceNotFoundException {
        // Crear y guardar un paciente
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle de Prueba");
        domicilio.setLocalidad("Ciudad");
        domicilio.setNumero(789);
        domicilio.setProvincia("Provincia");

        Paciente paciente = new Paciente();
        paciente.setNombre("Luis");
        paciente.setApellido("Martínez");
        paciente.setDni("11112222");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(domicilio);

        Paciente guardado = pacienteService.guardar(paciente);

        // Verificar que el paciente existe antes de eliminar
        assertNotNull(pacienteService.buscarPorId(guardado.getId()));

        // Eliminar el paciente
        pacienteService.eliminar(guardado.getId());

        // Verificar que el paciente ya no existe
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.buscarPorId(guardado.getId()));
    }

    @Test
    void testEliminarPacienteNoExistente() {
        // Intentar eliminar un paciente con un id no existente
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.eliminar(999L));
    }

    @Test
    void testActualizarPaciente() throws HandleConflictException, BadRequestException, ResourceNotFoundException {
        // Crear y guardar un paciente
        Domicilio domicilio = new Domicilio();
        domicilio.setCalle("Calle Vieja");
        domicilio.setLocalidad("Ciudad");
        domicilio.setNumero(101);
        domicilio.setProvincia("Provincia");

        Paciente paciente = new Paciente();
        paciente.setNombre("Carlos");
        paciente.setApellido("Hernández");
        paciente.setDni("22223333");
        paciente.setFechaAlta(LocalDate.now());
        paciente.setDomicilio(domicilio);

        Paciente guardado = pacienteService.guardar(paciente);

        // Actualizar el paciente
        guardado.setNombre("Carlos Actualizado");
        Paciente actualizado = pacienteService.actualizar(guardado);

        // Verificar que el paciente fue actualizado correctamente
        assertNotNull(actualizado);
        assertEquals("Carlos Actualizado", actualizado.getNombre());
        assertEquals("Hernández", actualizado.getApellido());
        assertEquals("22223333", actualizado.getDni());
    }

    @Test
    void testActualizarPacienteNoExistente() {
        // Intentar actualizar un paciente con un id no existente
        Paciente paciente = new Paciente();
        paciente.setId(999L);
        paciente.setNombre("Nonexistent");
        assertThrows(ResourceNotFoundException.class, () -> pacienteService.actualizar(paciente));
    }

    @Test
    void testListarPacientes() throws HandleConflictException, BadRequestException {
        // Crear y guardar algunos pacientes
        Domicilio domicilio1 = new Domicilio();
        domicilio1.setCalle("Calle Uno");
        domicilio1.setLocalidad("Ciudad");
        domicilio1.setNumero(111);
        domicilio1.setProvincia("Provincia");

        Paciente paciente1 = new Paciente();
        paciente1.setNombre("Pedro");
        paciente1.setApellido("López");
        paciente1.setDni("33334444");
        paciente1.setFechaAlta(LocalDate.now());
        paciente1.setDomicilio(domicilio1);
        pacienteService.guardar(paciente1);

        Domicilio domicilio2 = new Domicilio();
        domicilio2.setCalle("Calle Dos");
        domicilio2.setLocalidad("Ciudad");
        domicilio2.setNumero(222);
        domicilio2.setProvincia("Provincia");

        Paciente paciente2 = new Paciente();
        paciente2.setNombre("María");
        paciente2.setApellido("Fernández");
        paciente2.setDni("55556666");
        paciente2.setFechaAlta(LocalDate.now());
        paciente2.setDomicilio(domicilio2);
        pacienteService.guardar(paciente2);

        // Llamar al método de listar
        List<Paciente> pacientes = pacienteService.listar();

        // Verificar que la lista no es nula y contiene los pacientes agregados
        assertNotNull(pacientes);
        assertTrue(pacientes.size() >= 2);
        assertTrue(pacientes.stream().anyMatch(p -> p.getDni().equals("33334444")));
        assertTrue(pacientes.stream().anyMatch(p -> p.getDni().equals("55556666")));
    }
}
