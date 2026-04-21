package com.project.extension.repository;

import com.project.extension.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EnderecoRepository extends JpaRepository<Endereco, Integer> {
    List<Endereco> findAllByCepOrderByIdDesc(String cep);
}
