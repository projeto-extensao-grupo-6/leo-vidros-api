package com.project.extension.repository;

import com.project.extension.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByNomeIgnoreCase(String nome);
}
