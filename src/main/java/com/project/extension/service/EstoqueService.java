package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.EstoqueNaoEncontradoException;
import com.project.extension.exception.naopodesernegativo.EstoqueNaoPodeSerNegativoException;
import com.project.extension.repository.EstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EstoqueService {

    private final EstoqueRepository repository;
    private final ProdutoService produtoService;
    private final HistoricoEstoqueService historicoService;
    private final UsuarioService usuarioService;

    public Estoque entrada(Estoque request) {
        return movimentarEstoque(request, TipoMovimentacao.ENTRADA);
    }

    public Estoque saida(Estoque request) {
        return movimentarEstoque(request, TipoMovimentacao.SAIDA);
    }

    private Estoque movimentarEstoque(Estoque request, TipoMovimentacao tipo) {
        Produto produto = produtoService.buscarPorId(request.getProduto().getId());

        Estoque estoqueExistente = repository.findByProdutoAndLocalizacao(produto, request.getLocalizacao())
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setProduto(produto);
                    novo.setLocalizacao(request.getLocalizacao());
                    novo.setReservado(0);
                    novo.setQuantidade(0);
                    return novo;
                });

        int quantidadeAtual = estoqueExistente.getQuantidade() == null ? 0 : estoqueExistente.getQuantidade();
        int quantidadeMovimento = request.getQuantidade() == null ? 0 : request.getQuantidade();

        if (tipo == TipoMovimentacao.SAIDA) {
            if (quantidadeMovimento > quantidadeAtual) {
                throw new EstoqueNaoPodeSerNegativoException(
                        "Estoque insuficiente. Disponível: " + quantidadeAtual);
            }
            estoqueExistente.setQuantidade(quantidadeAtual - quantidadeMovimento);
        } else {
            estoqueExistente.setQuantidade(quantidadeAtual + quantidadeMovimento);
        }

        Estoque salvo = repository.save(estoqueExistente);

        Usuario usuarioLogado = getUsuarioLogado();
        HistoricoEstoque historico = new HistoricoEstoque();
        historico.setEstoque(salvo);
        historico.setUsuario(usuarioLogado);
        historico.setTipoMovimentacao(tipo);
        historico.setQuantidade(quantidadeMovimento);
        historico.setQuantidadeAtual(salvo.getQuantidade());
        historico.setObservacao(tipo == TipoMovimentacao.ENTRADA ? "Entrada de produto" : "Saída de produto");

        historicoService.cadastrar(historico);

        log.info("{} registrada: {} unidades de '{}' em '{}'. Quantidade atual: {}",
                tipo, quantidadeMovimento, produto.getNome(), request.getLocalizacao(), salvo.getQuantidade());

        return salvo;
    }

    public List<Estoque> listar() {
        List<Estoque> estoques = repository.findAll();
        log.info("Total de registros de estoque encontrados: {}", estoques.size());
        return estoques;
    }

    public Estoque buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    log.error("Estoque com ID {} não encontrado", id);
                    return new EstoqueNaoEncontradoException();
                });
    }

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioService.buscarPorEmail(email);
    }
}