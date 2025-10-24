package com.project.extension.repository;

import com.project.extension.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    Endereco findByCep(String cep);
}
