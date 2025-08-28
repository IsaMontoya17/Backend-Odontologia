package com.digitalhouse.odontologia.repository;

import com.digitalhouse.odontologia.entity.RoleEntity;
import com.digitalhouse.odontologia.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleEnum(Role roleEnum);
}