package com.project.extension.repository;

import com.project.extension.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {
    Funcionario findByTelefone(String telefone);
}
