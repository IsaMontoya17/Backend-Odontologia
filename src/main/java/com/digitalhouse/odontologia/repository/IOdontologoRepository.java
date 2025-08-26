package com.digitalhouse.odontologia.repository;

import com.digitalhouse.odontologia.entity.Odontologo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IOdontologoRepository extends JpaRepository<Odontologo, Long> {
    Optional<Odontologo> findByMatricula(String matricula);

    @Modifying
    @Transactional
    @Query("DELETE FROM Odontologo o WHERE o.matricula = ?1")
    void deleteByMatricula(String matricula);
}
