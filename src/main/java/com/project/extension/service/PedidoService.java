package com.project.extension.service;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
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
    private final PedidoContext pedidoContext;
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
        log.debug("passei aqui");
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
        log.info("Total de pedidos encontrados: " + pedidos.size() + " para etapa: " + etapa.getNome());
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

        logService.info(String.format(
                "Pedido ID %d excluído com sucesso.",
                id
        ));
    }

    public List<Pedido> listarPedidosPorTipo(String tipo) {
        return repository.findByTipoPedidoIgnoreCase(tipo);
    }
}
