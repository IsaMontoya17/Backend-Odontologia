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

    @Column(length = 100, nullable = false)
    private String calle;

    @Column(length = 50, nullable = false)
    private String numero;

    @Column(length = 50, nullable = false)
    private String barrio;

    @Column(length = 50, nullable = false)
    private String ciudad;

    @Column(length = 50, nullable = false)
    private String departamento;
}
