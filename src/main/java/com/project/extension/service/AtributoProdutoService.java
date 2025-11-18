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
    private final LogService logService;

    public AtributoProduto cadastrar(AtributoProduto atributoProduto, Produto produto) {
        atributoProduto.setProduto(produto);
        AtributoProduto atributoProdutoSalvo = repository.save(atributoProduto);
        String mensagem = String.format("Novo AtributoProduto ID: %d cadastrado para o Produto ID: %d. Tipo: %s, Valor: %s.",
                atributoProdutoSalvo.getId(),
                produto.getId(),
                atributoProdutoSalvo.getTipo(),
                atributoProdutoSalvo.getValor());
        logService.success(mensagem);

        return atributoProdutoSalvo;
    }

    public AtributoProduto buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            logService.error(String.format("Falha na busca: Atributo Produto com ID %d não encontrado.", id));
            log.warn("Atributo Produto com ID {} não encontrado", id);
            return new AtributoProdutoNaoEncontradoException();
        });
    }

    public List<AtributoProduto> listar() {
        List<AtributoProduto> atributoProdutos = repository.findAll();
        logService.info(String.format("Consulta a todos os Atributos de Produto. Total de registros: %d.", atributoProdutos.size()));
        return atributoProdutos;
    }

    public AtributoProduto editar(AtributoProduto origem, Integer id) {
        AtributoProduto destino = this.buscarPorId(id);

        this.atualizarDadosBasicos(destino, origem);

        AtributoProduto produtoAtualizado = repository.save(destino);
        String mensagem = String.format("AtributoProduto ID %d atualizado. Tipo: %s, Valor: %s.",
                produtoAtualizado.getId(),
                produtoAtualizado.getTipo(),
                produtoAtualizado.getValor());
        logService.info(mensagem);
        return produtoAtualizado;
    }

    public void deletar(Integer id) {
        AtributoProduto atributo = this.buscarPorId(id);

        repository.deleteById(id);
        log.info("Atributo Produto deletado com sucesso");
        String mensagem = String.format("AtributoProduto ID %d deletado. Produto ID associado: %d.",
                id,
                atributo.getProduto().getId());
        logService.info(mensagem);
    }

    private void atualizarDadosBasicos(AtributoProduto destino, AtributoProduto origem) {
        destino.setTipo(origem.getTipo());
        destino.setValor(origem.getValor());
    }
}
