package com.digitalhouse.odontologia.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class OdontologoResponseDTO {
    private String dni;
    private String apellido;
    private String nombre;
    private String email;
    private String telefono;
    private String especialidad;
    private Set<Long> turnosIds;
}