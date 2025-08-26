package com.digitalhouse.odontologia.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class PacienteResponseDTO {
    private String dni;
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private DomicilioResponseDTO domicilio;
    private Set<Long> turnosIds;
}
