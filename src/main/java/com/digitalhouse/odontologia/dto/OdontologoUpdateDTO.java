package com.digitalhouse.odontologia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OdontologoUpdateDTO {
    private String apellido;
    private String nombre;
    private String email;
    private String telefono;
    private String especialidad;
}
