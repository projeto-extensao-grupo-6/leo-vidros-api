package com.project.extension.service;

import com.project.extension.dto.itemproduto.ItemProdutoRequestDto;
import com.project.extension.dto.itemproduto.PedidoProdutoMapper;
import com.project.extension.dto.itemproduto.PedidoProdutoRequestDto;
import com.project.extension.dto.itemproduto.PedidoProdutoResponseDto;
import com.project.extension.entity.*;
import com.project.extension.exception.EstoqueInsuficienteException;
import com.project.extension.exception.naoencontrado.PedidoNaoEncontradoException;
import com.project.extension.repository.EstoqueRepository;
import com.project.extension.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {
    private final PedidoRepository repository;
    private final StatusService statusService;
    private final EtapaService etapaService;
    private final ClienteService clienteService;
    private final LogService logService;
    private final EstoqueRepository estoqueRepository;
    private final PedidoProdutoMapper pedidoProdutoMapper;


    @Transactional
    public PedidoProdutoResponseDto criarPedidoProduto(PedidoProdutoRequestDto dto) {
        // 1. CRIAÇÃO DO CABEÇALHO DO PEDIDO
        Pedido pedido = pedidoProdutoMapper.toEntity(dto);
        BigDecimal valorTotal = BigDecimal.ZERO;

        // 2. PROCESSAMENTO E VALIDAÇÃO DOS ITENS
        for (ItemProdutoRequestDto itemDto : dto.itens()) {
            Estoque estoque = estoqueRepository.findById(itemDto.estoqueId())
                    .orElseThrow(() -> new RuntimeException("Item de Estoque não encontrado."));

            // 3. VALIDAÇÃO CRÍTICA DE ESTOQUE
           if (itemDto.quantidadeSolicitada().compareTo(BigDecimal.valueOf(estoque.getQuantidadeDisponivel())) > 0)
           {
               String mensagem = String.format("Estoque insuficiente para o produto ID %d. Quantidade disponível: %d.",
                       estoque.getProduto().getId(),
                       estoque.getQuantidadeDisponivel());
               logService.error(mensagem);
               throw new EstoqueInsuficienteException(mensagem);
           }
           // 4. BAIXA DE ESTOQUE (Atualização da Entidade)
           estoque.setQuantidadeDisponivel(estoque.getQuantidadeDisponivel() - itemDto.quantidadeSolicitada().intValue());
           estoqueRepository.save(estoque);
           // 5. REGISTRO DO ITEM E CÁLCULO DO TOTAL
           ItemPedido itemPedido = pedidoProdutoMapper.toItemEntity(itemDto, pedido);
           BigDecimal subtotal = itemDto.quantidadeSolicitada().multiply(itemDto.precoUnitarioNegociado());
           itemPedido.setSubtotal(subtotal);
           valorTotal = valorTotal.add(subtotal);
           pedido.getItensPedido().add(itemPedido);
        }

        // 6. FINALIZAÇÃO E SALVAMENTO DO PEDIDO
        pedido.setValorTotal(valorTotal);
        Pedido pedidoSalvo = repository.save(pedido);

        // 7. LOG DE SUCESSO E RETORNO
        String mensagem = String.format("Novo Pedido ID %d (PRODUTO) cadastrado com sucesso. Total: %.2f.",
                pedidoSalvo.getId(), valorTotal);
        logService.success(mensagem);
        return pedidoProdutoMapper.toResponse(pedidoSalvo);
    }

    public Pedido cadastrar(Pedido pedido) {
        Status statusSalvo = statusService.buscarPorTipoAndStatus(
                pedido.getStatus().getTipo(),
                pedido.getStatus().getNome()
        );

        if (statusSalvo == null) {
            statusSalvo = statusService.cadastrar(pedido.getStatus());
            logService.info(String.format("Status criado automaticamente para Pedido: %s - %s.",
                    statusSalvo.getTipo(), statusSalvo.getNome()));
        }

        Etapa etapaSalvo = etapaService.buscarPorTipoAndEtapa(
                pedido.getEtapa().getTipo(),
                pedido.getEtapa().getNome()
        );

        if (etapaSalvo == null) {
            etapaSalvo = etapaService.cadastrar(pedido.getEtapa());
            logService.info(String.format("Etapa criada automaticamente para Pedido: %s - %s.",
                    etapaSalvo.getTipo(), etapaSalvo.getNome()));
        }

        Cliente clienteAssociado = clienteService.buscarPorId(
                pedido.getCliente().getId()
        );

        if (clienteAssociado == null){
            clienteAssociado = clienteService.cadastrar(pedido.getCliente());
            log.info("ID Client: {} - Cliente associado: {}", clienteAssociado.getId(), clienteAssociado.getNome());
        }

        pedido.setEtapa(etapaSalvo);
        pedido.setStatus(statusSalvo);

        Pedido pedidoSalvo = repository.save(pedido);
        String mensagem = String.format("Novo Pedido ID %d cadastrado com sucesso. Status: %s, Etapa: %s.",
                pedidoSalvo.getId(),
                statusSalvo.getNome(),
                etapaSalvo.getNome());
        logService.success(mensagem);

        return pedidoSalvo;
    }

    public Pedido buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Pedido com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.error("Pedido com ID {} não encontrado", id);
            return new PedidoNaoEncontradoException();
        });
    }

    public List<Pedido> listar() {
        List<Pedido> pedidos = repository.findAll();
        logService.info(String.format("Busca por todos os pedidos realizada. Total de registros: %d.", pedidos.size()));
        return pedidos;
    }

    private void atualizarCampos(Pedido destino, Pedido origem) {
       destino.setValorTotal(origem.getValorTotal());
       destino.setAtivo(origem.getAtivo());
       destino.setObservacao(origem.getObservacao());

        if (origem.getStatus() != null) {
            Status statusAtualizado = statusService.buscarPorTipoAndStatus(origem.getStatus().getTipo(),
                    origem.getStatus().getNome());
            destino.setStatus(statusAtualizado);
        }

        if (origem.getEtapa() != null) {
            Etapa etapaAtualizada = etapaService.buscarPorTipoAndEtapa(
                    origem.getEtapa().getTipo(),
                    origem.getEtapa().getNome()
            );
            destino.setEtapa(etapaAtualizada);
        }
    }

    public Pedido editar(Pedido origem, Integer id) {
        Pedido destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Pedido pedidoAtualizado = this.cadastrar(destino);
        String mensagem = String.format("Pedido ID %d atualizado com sucesso. Valor Total: %.2f.",
                pedidoAtualizado.getId(),
                pedidoAtualizado.getValorTotal());
        logService.info(mensagem);
        return pedidoAtualizado;
    }

    public void deletar(Integer id) {
        Pedido pedidoParaDeletar = this.buscarPorId(id);
        String mensagem = String.format("Pedido ID %d (Status: %s) deletado com sucesso.",
                id,
                pedidoParaDeletar.getStatus().getNome());
        logService.info(mensagem);
    }
}
