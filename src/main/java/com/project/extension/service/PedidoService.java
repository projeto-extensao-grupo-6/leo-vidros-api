// java
package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.PedidoNaoEncontradoException;
import com.project.extension.repository.PedidoRepository;
import com.project.extension.strategy.pedido.PedidoContext;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final EtapaService etapaService;
    private final StatusService statusService;
    private final ClienteService clienteService;
    private final PedidoContext pedidoContext;
    private final LogService logService;

    @Transactional
    public Pedido cadastrar(Pedido pedido) {

        // === STATUS ===
        Status statusSalvo = null;
        if (pedido.getStatus() != null) {
            statusSalvo = statusService.buscarPorTipoAndStatus(
                    pedido.getStatus().getTipo(),
                    pedido.getStatus().getNome()
            );

            if (statusSalvo == null) {
                statusSalvo = statusService.cadastrar(pedido.getStatus());
                logService.info(String.format(
                        "Status criado automaticamente: %s - %s.",
                        statusSalvo.getTipo(),
                        statusSalvo.getNome()
                ));
            }
        }

        // === ETAPA (pertence ao Servico do Pedido) ===
        Etapa etapaSalvo = null;
        if (pedido.getServico() != null && pedido.getServico().getEtapa() != null) {
            Etapa etapaPedido = pedido.getServico().getEtapa();
            etapaSalvo = etapaService.buscarPorTipoAndEtapa(
                    etapaPedido.getTipo(),
                    etapaPedido.getNome()
            );

            if (etapaSalvo == null) {
                etapaSalvo = etapaService.cadastrar(etapaPedido);
                logService.info(String.format(
                        "Etapa criada automaticamente: %s - %s.",
                        etapaSalvo.getTipo(),
                        etapaSalvo.getNome()
                ));
            }
        }

        // === CLIENTE ===
        Cliente clienteAssociado = null;

        if (pedido.getCliente() != null && pedido.getCliente().getId() != null) {
            clienteAssociado = clienteService.buscarPorId(pedido.getCliente().getId());
        }

        if (clienteAssociado == null && pedido.getCliente() != null) {
            clienteAssociado = clienteService.cadastrar(pedido.getCliente());
            if (clienteAssociado != null) {
                log.info("Cliente associado automaticamente. ID: {}, Nome: {}",
                        clienteAssociado.getId(),
                        clienteAssociado.getNome());
            } else {
                logService.error("Falha ao associar cliente automaticamente.");
            }
        }

        // Atualiza o pedido com os valores realmente salvos
        if (statusSalvo != null) {
            pedido.setStatus(statusSalvo);
        }
        if (pedido.getServico() != null && etapaSalvo != null) {
            pedido.getServico().setEtapa(etapaSalvo);
        }
        pedido.setCliente(clienteAssociado);

        // === PROCESSAMENTO VIA CONTEXT (REGRA DE NEGÓCIO CENTRAL) ===
        Pedido processado = pedidoContext.criar(pedido);
        Pedido salvo = repository.save(processado);

        logService.success(String.format(
                "Novo Pedido ID %d criado com sucesso. Tipo: %s, Total: %.2f.",
                salvo.getId(),
                salvo.getTipoPedido(),
                salvo.getValorTotal()
        ));

        return salvo;
    }

    public Pedido buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String msg = String.format("Pedido ID %d não encontrado.", id);
            logService.error(msg);
            return new PedidoNaoEncontradoException();
        });
    }

    public List<Pedido> listar() {
        List<Pedido> pedidos = repository.findAll();
        logService.info(String.format("Listagem de pedidos: %d registros.", pedidos.size()));
        return pedidos;
    }

    public List<Pedido> listarPedidosPorTipoENomeDaEtapa(String nome) {
        Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", nome);
        List<Pedido> pedidos = repository.findAllByServico_Etapa(etapa);
        log.info("Total de pedidos encontrados: {} para etapa {}", pedidos.size(), etapa.getNome());
        return pedidos;
    }

    @Transactional
    public Pedido editar(Integer id, Pedido pedidoAtualizar) {
        Pedido pedidoAntigo = buscarPorId(id);
        pedidoAntigo.setId(id);

        Pedido processado = pedidoContext.editar(pedidoAntigo, pedidoAtualizar);
        Pedido salvo = repository.save(processado);

        logService.info(String.format(
                "Pedido ID %d atualizado com sucesso. Total: %.2f.",
                salvo.getId(),
                salvo.getValorTotal()
        ));

        return salvo;
    }

    @Transactional
    public void deletar(Integer id) {
        Pedido pedido = buscarPorId(id);
        pedidoContext.deletar(pedido);
        repository.delete(pedido);

        logService.info(String.format("Pedido ID %d excluído com sucesso.", id));
    }

    public List<Pedido> listarPedidosPorTipo(String tipo) {
        return repository.findByTipoPedidoIgnoreCase(tipo);
    }
}
