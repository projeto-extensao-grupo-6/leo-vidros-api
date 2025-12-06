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
        return movimentarEstoque(request, TipoMovimentacao.ENTRADA, null, null);
    }

    public Estoque saida(Estoque request){
        return movimentarEstoque(request, TipoMovimentacao.SAIDA, null, OrigemMovimentacao.MANUAL, null);
    }

    public void saida(Estoque request, Pedido pedido) {
        movimentarEstoque(request, TipoMovimentacao.SAIDA, pedido, OrigemMovimentacao.PEDIDO);
    }

    public Estoque saida(Estoque request, OrigemMovimentacao origemMovimentacao, MotivoPerda motivoPerda) {
        return movimentarEstoque(request, TipoMovimentacao.SAIDA, null, origemMovimentacao, motivoPerda);
    }

    private Estoque movimentarEstoque(
            Estoque request,
            TipoMovimentacao tipo,
            Pedido pedido,
            OrigemMovimentacao origem
    ) {
        return movimentarEstoque(request, tipo, pedido, origem, null);
    }

    private Estoque movimentarEstoque(
            Estoque request,
            TipoMovimentacao tipo,
            Pedido pedido,
            OrigemMovimentacao origem,
            MotivoPerda motivoPerda
    ) {
        validarRequest(request);

        Produto produto = produtoService.buscarPorId(request.getProduto().getId());
        Estoque estoque = buscarOuCriarEstoque(produto, request.getLocalizacao());

        BigDecimal quantidade = validarQuantidade(request.getQuantidadeTotal());

        BigDecimal totalAtual = estoque.getQuantidadeTotal();
        BigDecimal novoTotal = calcularNovoTotal(tipo, totalAtual, estoque.getQuantidadeDisponivel(), quantidade, produto);

        aplicarNovoTotal(estoque, novoTotal);
        atualizarStatusProduto(produto, novoTotal);

        Estoque salvo = repository.save(estoque);

        registrarHistorico(salvo, tipo, pedido, origem, motivoPerda, quantidade, produto);

        logMovimentacao(tipo, quantidade, produto, salvo);

        return salvo;
    }

    private void validarRequest(Estoque request) {
        if (request.getProduto() == null) {
            log.error("Para movimentar estoque, deve ter um produto!");
            throw new IllegalArgumentException("Produto é obrigatório.");
        }
    }

    private Estoque buscarOuCriarEstoque(Produto produto, String localizacao) {
        return repository.findByProdutoAndLocalizacao(produto, localizacao)
                .orElseGet(() -> {
                    Estoque novo = new Estoque();
                    novo.setProduto(produto);
                    novo.setLocalizacao(localizacao);
                    novo.setQuantidadeTotal(BigDecimal.ZERO);
                    novo.setQuantidadeDisponivel(BigDecimal.ZERO);
                    novo.setReservado(BigDecimal.ZERO);

                    logService.warning(String.format(
                            "Novo registro de estoque criado implicitamente para Produto ID %d em %s.",
                            produto.getId(), localizacao
                    ));

                    return novo;
                });
    }

    private BigDecimal validarQuantidade(BigDecimal quantidade) {
        if (quantidade == null || quantidade.compareTo(BigDecimal.ZERO) <= 0) {
            logService.error("Tentativa de movimentação com quantidade inválida.");
            throw new IllegalArgumentException("A quantidade movimentada deve ser maior que zero.");
        }
        return quantidade;
    }

    private BigDecimal calcularNovoTotal(
            TipoMovimentacao tipo,
            BigDecimal totalAtual,
            BigDecimal disponivelAtual,
            BigDecimal quantidade,
            Produto produto
    ) {
        if (tipo == TipoMovimentacao.SAIDA) {

            if (quantidade.compareTo(disponivelAtual) > 0) {
                throw new EstoqueNaoPodeSerNegativoException(String.format(
                        "Estoque insuficiente para '%s'. Disponível: %f, solicitado: %f.",
                        produto.getNome(), disponivelAtual, quantidade
                ));
            }

            return totalAtual.subtract(quantidade);
        }

        return totalAtual.add(quantidade);
    }

    private void aplicarNovoTotal(Estoque estoque, BigDecimal novoTotal) {
        estoque.setQuantidadeTotal(novoTotal);
        estoque.setQuantidadeDisponivel(novoTotal.subtract(estoque.getReservado()));
    }

    private void atualizarStatusProduto(Produto produto, BigDecimal novoTotal) {
        if (produto == null || novoTotal == null) return;

        boolean estavaAtivo = Boolean.TRUE.equals(produto.getAtivo());
        boolean deveFicarAtivo = novoTotal.compareTo(BigDecimal.ZERO) > 0;

        if (deveFicarAtivo != estavaAtivo) {
            produto.setAtivo(deveFicarAtivo);
            produtoService.editar(produto, produto.getId());
        }
    }

    private void registrarHistorico(
            Estoque estoque,
            TipoMovimentacao tipo,
            Pedido pedido,
            OrigemMovimentacao origem,
            MotivoPerda motivoPerda,
            BigDecimal quantidade,
            Produto produto
    ) {
        Usuario usuario = getUsuarioLogado();

        HistoricoEstoque historico = new HistoricoEstoque();
        historico.setEstoque(estoque);
        historico.setUsuario(usuario);
        historico.setTipoMovimentacao(tipo);
        historico.setQuantidade(quantidade);
        historico.setQuantidadeAtual(estoque.getQuantidadeDisponivel());

        if (tipo == TipoMovimentacao.SAIDA) {
            if (pedido != null) {
                historico.setPedido(pedido);
                historico.setOrigem(OrigemMovimentacao.PEDIDO);
            } else if (origem == OrigemMovimentacao.PERDA) {
                historico.setOrigem(OrigemMovimentacao.PERDA);
                historico.setMotivoPerda(motivoPerda);
            } else {
                historico.setOrigem(OrigemMovimentacao.MANUAL);
            }
        } else {
            historico.setOrigem(OrigemMovimentacao.MANUAL);
        }

        historico.setObservacao(
                tipo == TipoMovimentacao.ENTRADA
                        ? String.format("Entrada de %f unidades de '%s' em '%s'",
                        quantidade, produto.getNome(), estoque.getLocalizacao())
                        : String.format("Saída de %f unidades de '%s' em '%s'",
                        quantidade, produto.getNome(), estoque.getLocalizacao())
        );

        historicoService.cadastrar(historico);
    }

    private void logMovimentacao(
            TipoMovimentacao tipo,
            BigDecimal quantidade,
            Produto produto,
            Estoque estoque
    ) {
        Usuario usuario = getUsuarioLogado();

        logService.info(String.format(
                "Movimentação de estoque (Tipo: %s) por Usuário ID %d. Produto: '%s', Quantidade: %f, Novo Total: %f.",
                tipo, usuario.getId(), produto.getNome(), quantidade, estoque.getQuantidadeTotal()
        ));
    }

    public List<Estoque> listar() { return repository.findAll(); }

    public Estoque buscarPorId(Integer id) {
        return repository.findById(id)
                .orElseThrow(EstoqueNaoEncontradoException::new);
    }

    public void reservarProduto(Produto produto, BigDecimal quantidade) {
        Estoque estoque = repository.findByProdutoId(produto.getId())
                .orElseThrow(EstoqueNaoEncontradoException::new);

        BigDecimal reservadoAtual = estoque.getReservado();
        BigDecimal disponivel = estoque.getQuantidadeDisponivel();

        if (quantidade.compareTo(disponivel) > 0) {
            throw new EstoqueNaoPodeSerNegativoException("Estoque insuficiente para reserva.");
        }

        estoque.setReservado(reservadoAtual.add(quantidade));
        estoque.setQuantidadeDisponivel(disponivel.subtract(quantidade));

        repository.save(estoque);
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