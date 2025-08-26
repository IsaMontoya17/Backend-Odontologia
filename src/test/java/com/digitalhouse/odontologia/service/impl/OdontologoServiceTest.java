package com.digitalhouse.odontologia.service.impl;

import com.digitalhouse.odontologia.entity.Odontologo;
import com.digitalhouse.odontologia.exception.HandleConflictException;
import com.digitalhouse.odontologia.exception.ResourceNotFoundException;
import com.digitalhouse.odontologia.repository.IOdontologoRepository;
import com.digitalhouse.odontologia.service.IOdontologoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OdontologoServiceTest {

    @Autowired
    private IOdontologoRepository odontologoRepository;

    @Autowired
    private IOdontologoService odontologoService;

    @Test
    void testGuardarOdontologo() throws HandleConflictException {
        // Crear un nuevo odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Juan");
        odontologo.setApellido("Pérez");
        odontologo.setMatricula("12345");

        // Guardar el odontólogo
        Odontologo guardado = odontologoService.guardar(odontologo);

        // Verificar que el odontólogo fue guardado correctamente
        assertNotNull(guardado.getId());
        assertEquals("Juan", guardado.getNombre());
        assertEquals("Pérez", guardado.getApellido());
        assertEquals("12345", guardado.getMatricula());
    }

    @Test
    void testGuardarOdontologoConMatriculaDuplicada() throws HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo1 = new Odontologo();
        odontologo1.setNombre("Pedro");
        odontologo1.setApellido("Gómez");
        odontologo1.setMatricula("54321");
        odontologoService.guardar(odontologo1);

        // Intentar guardar otro odontólogo con la misma matrícula
        Odontologo odontologo2 = new Odontologo();
        odontologo2.setNombre("Ana");
        odontologo2.setApellido("Martínez");
        odontologo2.setMatricula("54321");

        assertThrows(HandleConflictException.class, () -> odontologoService.guardar(odontologo2));
    }

    @Test
    void testBuscarPorId() throws HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Ana");
        odontologo.setApellido("Gómez");
        odontologo.setMatricula("67890");

        Odontologo guardado = odontologoService.guardar(odontologo);

        // Buscar por id
        Odontologo encontrado = odontologoService.buscarPorId(guardado.getId());

        // Verificar que el odontólogo fue encontrado correctamente
        assertNotNull(encontrado);
        assertEquals(guardado.getId(), encontrado.getId());
        assertEquals("Ana", encontrado.getNombre());
        assertEquals("Gómez", encontrado.getApellido());
        assertEquals("67890", encontrado.getMatricula());
    }

    @Test
    void testBuscarPorIdNoExistente() {
        // Intentar buscar un odontólogo con un id no existente
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.buscarPorId(999L));
    }

    @Test
    void testEliminarOdontologo() throws HandleConflictException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Luis");
        odontologo.setApellido("Martínez");
        odontologo.setMatricula("54321");

        Odontologo guardado = odontologoService.guardar(odontologo);
        assertNotNull(odontologoService.buscarPorId(guardado.getId()));

        // Eliminar el odontólogo
        odontologoService.eliminar(guardado.getId());

        // Verificar que el odontólogo ya no existe
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.buscarPorId(guardado.getId()));
    }

    @Test
    void testActualizarOdontologo() throws HandleConflictException, ResourceNotFoundException {
        // Crear y guardar un odontólogo
        Odontologo odontologo = new Odontologo();
        odontologo.setNombre("Carlos");
        odontologo.setApellido("Hernández");
        odontologo.setMatricula("11223");
        Odontologo guardado = odontologoService.guardar(odontologo);

        // Actualizar el odontólogo
        guardado.setNombre("Carlos Updated");
        Odontologo actualizado = odontologoService.actualizar(guardado);

        // Verificar que el odontólogo fue actualizado correctamente
        assertNotNull(actualizado);
        assertEquals("Carlos Updated", actualizado.getNombre());
        assertEquals("Hernández", actualizado.getApellido());
        assertEquals("11223", actualizado.getMatricula());
    }

    @Test
    void testActualizarOdontologoNoExistente() {
        // Intentar actualizar un odontólogo con un id no existente
        Odontologo odontologo = new Odontologo();
        odontologo.setId(999L);
        odontologo.setNombre("Nonexistent");
        assertThrows(ResourceNotFoundException.class, () -> odontologoService.actualizar(odontologo));
    }

    @Test
    void testListarOdontologos() throws HandleConflictException {
        odontologoRepository.deleteAll();

        // Crear y guardar algunos odontólogos
        Odontologo odontologo1 = new Odontologo();
        odontologo1.setNombre("Luis");
        odontologo1.setApellido("Martínez");
        odontologo1.setMatricula("54321");
        odontologoService.guardar(odontologo1);

        Odontologo odontologo2 = new Odontologo();
        odontologo2.setNombre("Carlos");
        odontologo2.setApellido("Hernández");
        odontologo2.setMatricula("11223");
        odontologoService.guardar(odontologo2);

        List<Odontologo> odontologos = odontologoService.listar();
        assertNotNull(odontologos);
        assertEquals(2, odontologos.size());
        assertTrue(odontologos.stream().anyMatch(o -> o.getMatricula().equals("54321")));
        assertTrue(odontologos.stream().anyMatch(o -> o.getMatricula().equals("11223")));
    }
}