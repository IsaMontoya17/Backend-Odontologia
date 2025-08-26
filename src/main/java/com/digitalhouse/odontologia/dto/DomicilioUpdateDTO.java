package com.digitalhouse.odontologia.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DomicilioUpdateDTO {
    private String calle;
    private String numero;
    private String barrio;
    private String ciudad;
    private String departamento;
}
