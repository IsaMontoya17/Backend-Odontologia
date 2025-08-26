package com.digitalhouse.odontologia.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "Domicilios")
@Getter
@Setter
@ToString
public class Domicilio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String calle;
    private String localidad;
    private Integer numero;
    private String provincia;

}
