package com.project.extension.integration;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.Produto;
import com.project.extension.repository.EstoqueRepository;
import com.project.extension.repository.ProdutoRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ProdutoRepositoryIT extends AbstractIntegrationIT {

    @Autowired ProdutoRepository produtoRepository;
    @Autowired EstoqueRepository estoqueRepository;

    @AfterEach
    void limpar() {
        estoqueRepository.deleteAll();
        produtoRepository.deleteAll();
    }

    @Test
    void deveSalvarEBuscarProdutoPorId() {
        Produto produto = new Produto("Vidro 8mm", "Vidro temperado", "m²", 120.0, true);
        Produto salvo = produtoRepository.save(produto);

        assertNotNull(salvo.getId());

        Optional<Produto> encontrado = produtoRepository.findById(salvo.getId());
        assertTrue(encontrado.isPresent());
        assertEquals("Vidro 8mm", encontrado.get().getNome());
        assertTrue(encontrado.get().getAtivo());
    }

    @Test
    void deveBuscarEstoquePorProdutoELocalizacao() {
        Produto produto = produtoRepository.save(
                new Produto("Vidro 6mm", "Vidro simples", "m²", 80.0, true));

        Estoque estoque = new Estoque("Depósito A", BigDecimal.valueOf(50));
        estoque.setProduto(produto);
        estoque.setQuantidadeDisponivel(BigDecimal.valueOf(50));
        estoque.setReservado(BigDecimal.ZERO);
        estoqueRepository.save(estoque);

        Optional<Estoque> encontrado = estoqueRepository.findByProdutoAndLocalizacao(produto, "Depósito A");

        assertTrue(encontrado.isPresent());
        assertEquals(0, BigDecimal.valueOf(50).compareTo(encontrado.get().getQuantidadeTotal()));
        assertEquals("Depósito A", encontrado.get().getLocalizacao());
    }

    @Test
    void deveRetornarVazioQuandoProdutoNaoExiste() {
        Optional<Produto> encontrado = produtoRepository.findById(Integer.MAX_VALUE);
        assertTrue(encontrado.isEmpty());
    }
}
