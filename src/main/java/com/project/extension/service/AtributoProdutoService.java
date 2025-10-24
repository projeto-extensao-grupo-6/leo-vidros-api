package com.project.extension.service;

import com.project.extension.entity.AtributoProduto;
import com.project.extension.entity.Produto;
import com.project.extension.exception.naoencontrado.AtributoProdutoNaoEncontradoException;
import com.project.extension.repository.AtributoProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class AtributoProdutoService {

    private final AtributoProdutoRepository repository;

    public AtributoProduto cadastrar(AtributoProduto atributoProduto, Produto produto) {
        atributoProduto.setProduto(produto);

        AtributoProduto atributoProdutoSalvo = repository.save(atributoProduto);
        log.info("Atributo Produto salvo com sucesso!");
        return atributoProdutoSalvo;
    }


    public AtributoProduto buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Atributo Produto com ID " + id + " não encontrado");
            return new AtributoProdutoNaoEncontradoException();
        });
    }

    public List<AtributoProduto> listar() {
        List<AtributoProduto> atributoProdutos = repository.findAll();
        log.info("Total de produtos encontrados: " + atributoProdutos.size());
        return atributoProdutos;
    }

    public AtributoProduto editar(AtributoProduto origem, Integer id) {
        AtributoProduto destino = this.buscarPorId(id);

        this.atualizarDadosBasicos(destino, origem);

        AtributoProduto produtoAtualizado = repository.save(destino);
        log.info("Atributo Produto atualizado com sucesso!");
        return produtoAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Atributo Produto deletado com sucesso");
    }

    private void atualizarDadosBasicos(AtributoProduto destino, AtributoProduto origem) {
        destino.setTipo(origem.getTipo());
        destino.setValor(origem.getValor());
    }
}
