package com.project.extension.repository;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Integer> {
    Optional<Estoque> findByProdutoAndLocalizacao(Produto produto, String localizacao);

    Optional<Estoque> findByProduto(Produto produto);

    @Query("""
        SELECT COUNT(*)
        FROM estoque e
        JOIN produto p ON p.id = e.produto_id
        JOIN metrica_estoque m ON m.id = p.metrica_estoque_id
        WHERE e.quantidade_disponivel < m.nivel_minimo
    """)
    int countItensAbaixoMinimo();

    @Query("""
        SELECT
        e.quantidade_total,
        e.quantidade_disponivel,
        e.reservado,
        e.localizacao,
        p.nome,
        p.descricao,
        p.unidade_medida,
        p.preco,
        m.nivel_minimo,
        m.nivel_maximo
        FROM estoque e
        JOIN produto p ON p.id = e.produto_id
        JOIN metrica_estoque m ON m.id = p.metrica_estoque_id
        WHERE e.quantidade_disponivel < m.nivel_minimo
    """)
    int aa();
}
