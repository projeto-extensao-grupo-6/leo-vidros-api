package com.project.extension.repository;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Optional<Estoque> findByProdutoAndLocalizacao(Produto produto, String localizacao);

    Optional<Estoque> findByProduto(Produto produto);
}
