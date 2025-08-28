package com.digitalhouse.odontologia.repository;

import com.digitalhouse.odontologia.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IPermissionRepository extends JpaRepository<PermissionEntity, Long> {
    Optional<PermissionEntity> findByName(String name);
}
