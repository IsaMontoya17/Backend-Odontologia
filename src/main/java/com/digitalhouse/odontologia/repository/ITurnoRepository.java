package com.digitalhouse.odontologia.repository;

import com.digitalhouse.odontologia.entity.Turno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ITurnoRepository extends JpaRepository<Turno, Long> {
    @Query("SELECT t FROM Turno t WHERE t.fecha = ?1")
    List<Turno> findByFecha(LocalDate fecha);

}
