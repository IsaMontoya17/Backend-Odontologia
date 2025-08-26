package com.digitalhouse.odontologia.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PacienteUpdateDTO {
    private String nombre;
    private String apellido;
    private LocalDate fechaNacimiento;
    private DomicilioUpdateDTO domicilio;
}
