package com.project.extension.service;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
import com.project.extension.exception.naoencontrado.PedidoNaoEncontradoException;
import com.project.extension.repository.HistoricoEstoqueRepository;
import com.project.extension.repository.OrcamentoRepository;
import com.project.extension.repository.PedidoRepository;
import com.project.extension.strategy.pedido.PedidoContext;
import org.springframework.transaction.annotation.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final EtapaService etapaService;
    private final PedidoContext pedidoContext;
    private final AgendamentoService agendamentoService;
    private final HistoricoEstoqueRepository historicoEstoqueRepository;
    private final OrcamentoRepository orcamentoRepository;
    private final LogService logService;

    @Transactional
    public Pedido cadastrar(Pedido pedido) {

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

    public Page<Pedido> listar(Pageable pageable) {
        Page<Pedido> pedidos = repository.findAll(pageable);
        logService.info(String.format("Listagem de pedidos: %d registros.", pedidos.getTotalElements()));
        return pedidos;
    }

    public Page<Pedido> listarPedidosPorTipoENomeDaEtapa(String nome, Pageable pageable) {
        Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", nome);
        Page<Pedido> pedidos = repository.findAllByServico_Etapa(etapa, pageable);
        log.info("Total de pedidos encontrados: {} para etapa: {}", pedidos.getTotalElements(), etapa.getNome());
        return pedidos;
    }

    @Transactional
    public Pedido editar(Integer id, Pedido pedidoAtualizar) {
        Pedido pedidoAntigo = buscarPorId(id);
        log.debug(String.valueOf(pedidoAntigo.getId()));
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

        if (pedido.getServico() != null && pedido.getServico().getAgendamentos() != null) {
            var agendamentos = new ArrayList<>(pedido.getServico().getAgendamentos());
            for (var agendamento : agendamentos) {
                agendamentoService.deletar(agendamento.getId());
            }
        }

        pedidoContext.deletar(pedido);
        orcamentoRepository.deleteByPedidoId(pedido.getId());
        historicoEstoqueRepository.deleteByPedidoId(pedido.getId());
        repository.delete(pedido);

        logService.info(String.format(
                "Pedido ID %d excluído com sucesso.",
                id
        ));
    }

    public Page<Pedido> listarPedidosPorTipo(String tipo, Pageable pageable) {
        return repository.findByTipoPedidoIgnoreCase(tipo, pageable);
    }

    public Page<Pedido> listarPedidosDeServico(Pageable pageable) {
        return repository.findByServicoIsNotNull(pageable);
    }

    public Page<Pedido> listarPedidosDeProduto(Pageable pageable) {
        return repository.findByItensPedidoIsNotEmpty(pageable);
    }
}
