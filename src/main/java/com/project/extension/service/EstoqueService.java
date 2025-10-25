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
                    novo.setQuantidadeTotal(0);
                    novo.setQuantidadeDisponivel(0);
                    novo.setReservado(0);
                    return novo;
                });

        int totalAtual = estoqueExistente.getQuantidadeTotal() != null ? estoqueExistente.getQuantidadeTotal() : 0;
        int disponivelAtual = estoqueExistente.getQuantidadeDisponivel() != null ? estoqueExistente.getQuantidadeDisponivel() : 0;

        int quantidadeMovimento = request.getQuantidadeTotal() != null ? request.getQuantidadeTotal() : 0;

        if (quantidadeMovimento <= 0) {
            throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero.");
        }

        if (tipo == TipoMovimentacao.SAIDA) {
            if (quantidadeMovimento > disponivelAtual) {
                throw new EstoqueNaoPodeSerNegativoException(
                        String.format("Estoque insuficiente. Disponível: %d, solicitado: %d", disponivelAtual, quantidadeMovimento)
                );
            }

            estoqueExistente.setQuantidadeTotal(totalAtual - quantidadeMovimento);
            estoqueExistente.setQuantidadeDisponivel((totalAtual - quantidadeMovimento) - estoqueExistente.getReservado());

        } else if (tipo == TipoMovimentacao.ENTRADA) {
            estoqueExistente.setQuantidadeTotal(totalAtual + quantidadeMovimento);
            estoqueExistente.setQuantidadeDisponivel((totalAtual + quantidadeMovimento) - estoqueExistente.getReservado());
        }

        Estoque salvo = repository.save(estoqueExistente);

        Usuario usuarioLogado = getUsuarioLogado();

        HistoricoEstoque historico = new HistoricoEstoque();
        historico.setEstoque(salvo);
        historico.setUsuario(usuarioLogado);
        historico.setTipoMovimentacao(tipo);
        historico.setQuantidade(quantidadeMovimento);
        historico.setQuantidadeAtual(salvo.getQuantidadeDisponivel());
        historico.setObservacao(
                tipo == TipoMovimentacao.ENTRADA
                        ? String.format("Entrada de %d unidades de '%s' em '%s'", quantidadeMovimento, produto.getNome(), salvo.getLocalizacao())
                        : String.format("Saída de %d unidades de '%s' em '%s'", quantidadeMovimento, produto.getNome(), salvo.getLocalizacao())
        );

        historicoService.cadastrar(historico);

        log.info("{} registrada: {} unidades de '{}' em '{}'. Disponível agora: {} | Total: {}",
                tipo, quantidadeMovimento, produto.getNome(),
                salvo.getLocalizacao(),
                salvo.getQuantidadeDisponivel(),
                salvo.getQuantidadeTotal());

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

    public Estoque reservarProduto(Produto produto, Integer quantidade) {
        Estoque estoque = buscarPorId(produto.getId());

        int reservadoAtual = estoque.getReservado() != null ? estoque.getReservado() : 0;
        int disponivel = estoque.getQuantidadeDisponivel() != null ? estoque.getQuantidadeDisponivel() : 0;

        if (quantidade > disponivel) {
            throw new EstoqueNaoPodeSerNegativoException(
                    String.format("Estoque insuficiente. Disponível: %d, solicitado: %d", disponivel, quantidade)
            );
        }

        estoque.setReservado(reservadoAtual + quantidade);
        estoque.setQuantidadeDisponivel(disponivel - quantidade);

        Estoque estoqueAtualizado = repository.save(estoque);

        log.info(
                "Produto '{}' reservado com sucesso: {} unidades. Total reservado agora: {}",
                produto.getNome(),
                quantidade,
                estoqueAtualizado.getReservado()
        );

        return estoqueAtualizado;
    }

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioService.buscarPorEmail(email);
    }
}