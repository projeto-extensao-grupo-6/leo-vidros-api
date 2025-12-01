package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.EstoqueNaoEncontradoException;
import com.project.extension.exception.naoencontrado.ProdutoNaoEncontradoException;
import com.project.extension.exception.naopodesernegativo.EstoqueNaoPodeSerNegativoException;
import com.project.extension.repository.EstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EstoqueService {

    private final EstoqueRepository repository;
    private final ProdutoService produtoService;
    private final HistoricoEstoqueService historicoService;
    private final UsuarioService usuarioService;
    private final LogService logService;

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
                    novo.setQuantidadeTotal(BigDecimal.ZERO);
                    novo.setQuantidadeDisponivel(BigDecimal.ZERO);
                    novo.setReservado(BigDecimal.ZERO);
                    logService.warning(String.format(
                            "Novo registro de estoque criado implicitamente para Produto ID %d em %s durante movimentação de %s.",
                            produto.getId(), novo.getLocalizacao(), tipo));
                    return novo;
                });

        BigDecimal totalAtual = estoqueExistente.getQuantidadeTotal() != null ? estoqueExistente.getQuantidadeTotal() : BigDecimal.ZERO;
        BigDecimal disponivelAtual = estoqueExistente.getQuantidadeDisponivel() != null ? estoqueExistente.getQuantidadeDisponivel() : BigDecimal.ZERO;

        BigDecimal quantidadeMovimento = request.getQuantidadeTotal() != null ? request.getQuantidadeTotal() : BigDecimal.ZERO;

        if (quantidadeMovimento.compareTo(BigDecimal.ZERO) <= 0) {
            logService.error("Tentativa de movimentação de estoque com quantidade menor ou igual a zero.");
            throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero.");
        }

        if (tipo == TipoMovimentacao.SAIDA) {
            if (quantidadeMovimento.compareTo(disponivelAtual) > 0) {
                String erroMensagem = String.format("Estoque insuficiente para Produto '%s'. Disponível: %f, solicitado: %f.",
                        produto.getNome(), disponivelAtual, quantidadeMovimento);
                logService.warning(erroMensagem);
                throw new EstoqueNaoPodeSerNegativoException(erroMensagem);
            }

            BigDecimal novoTotal = totalAtual.subtract(quantidadeMovimento);
            estoqueExistente.setQuantidadeTotal(novoTotal);
            BigDecimal novoDisponivel = totalAtual.subtract(estoqueExistente.getReservado());
            estoqueExistente.setQuantidadeDisponivel(novoDisponivel);

        } else {
            BigDecimal novoTotal = totalAtual.add(quantidadeMovimento);
            estoqueExistente.setQuantidadeTotal(novoTotal);
            BigDecimal novoDisponivel = (totalAtual.add(quantidadeMovimento)).subtract(estoqueExistente.getReservado());
            estoqueExistente.setQuantidadeDisponivel(novoDisponivel);
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
                        ? String.format("Entrada de %f unidades de '%s' em '%s'", quantidadeMovimento, produto.getNome(), salvo.getLocalizacao())
                        : String.format("Saída de %f unidades de '%s' em '%s'", quantidadeMovimento, produto.getNome(), salvo.getLocalizacao())
        );

        historicoService.cadastrar(historico);

        String logMovimento = String.format("Movimentação de estoque (Tipo: %s) registrada por Usuário ID %d. Produto: '%s', Quantidade: %f, Novo Total: %f.",
                tipo, usuarioLogado.getId(), produto.getNome(), quantidadeMovimento, salvo.getQuantidadeTotal());
        logService.info(logMovimento);

        return salvo;
    }

    public List<Estoque> listar() {
        List<Estoque> estoques = repository.findAll();
        logService.info(String.format("Busca por todos os registros de estoque realizada. Total de registros: %d.", estoques.size()));
        return estoques;
    }

    public Estoque buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logService.error(String.format("Falha na busca: Estoque com ID %d não encontrado.", id));
                    log.error("Estoque com ID {} não encontrado", id);
                    return new EstoqueNaoEncontradoException();
                });
    }

    public Estoque reservarProduto(Produto produto, BigDecimal quantidade) {
        Estoque estoque = buscarPorId(produto.getId());

        BigDecimal reservadoAtual = estoque.getReservado() != null ? estoque.getReservado() : BigDecimal.ZERO;
        BigDecimal disponivel = estoque.getQuantidadeDisponivel() != null ? estoque.getQuantidadeDisponivel() : BigDecimal.ZERO;

        if (quantidade.compareTo(disponivel) > 0) {
            String erroMensagem = String.format("Estoque insuficiente para reserva do Produto '%s'. Disponível: %f, solicitado: %f.",
                    produto.getNome(), disponivel, quantidade);
            logService.warning(erroMensagem);
            throw new EstoqueNaoPodeSerNegativoException(erroMensagem);
        }

        estoque.setReservado(reservadoAtual.add(quantidade));
        estoque.setQuantidadeDisponivel(reservadoAtual.add(quantidade));

        Estoque estoqueAtualizado = repository.save(estoque);

        String logReserva = String.format(
                "Produto '%s' reservado com sucesso: %f unidades. Total reservado agora: %f. Registro Estoque ID: %d.",
                produto.getNome(),
                quantidade,
                estoqueAtualizado.getReservado(),
                estoqueAtualizado.getId()
        );
        logService.info(logReserva);

        return estoqueAtualizado;
    }

    private Usuario getUsuarioLogado() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return usuarioService.buscarPorEmail(email);
    }

    public Estoque buscarEstoquePorIdProduto(Produto produto) {
        return repository.findByProduto(produto).orElseThrow(ProdutoNaoEncontradoException::new);
    }
}