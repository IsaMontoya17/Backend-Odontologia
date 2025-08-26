package com.digitalhouse.odontologia.repository;

import com.digitalhouse.odontologia.entity.Odontologo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IOdontologoRepository extends JpaRepository<Odontologo, String> {
    Optional<Odontologo> findByEmail(String email);
    List<Odontologo> findByEspecialidad(String especialidad);
}
