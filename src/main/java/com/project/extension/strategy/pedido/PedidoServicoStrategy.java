package com.project.extension.strategy.pedido;

import com.project.extension.entity.*;
import com.project.extension.service.ClienteService;
import com.project.extension.service.EtapaService;
import com.project.extension.service.ServicoService;
import com.project.extension.service.StatusService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component("PEDIDO_SERVICO")
@AllArgsConstructor
@Slf4j
public class PedidoServicoStrategy implements PedidoStrategy {

    private final ServicoService servicoService;
    private final StatusService statusService;
    private final ClienteService clienteService;
    private final EtapaService etapaService;

    @Override
    public Pedido criar(Pedido pedido) {
        if (pedido.getCliente().getId() == 0 ) {
            Cliente cliente = clienteService.cadastrar(pedido.getCliente());
            pedido.setCliente(cliente);
        }

        if (pedido.getCliente() != null) {
            Cliente cliente = clienteService.buscarPorId(pedido.getCliente().getId());
            pedido.setCliente(cliente);
        }

        Servico servico = pedido.getServico();
        if (servico == null) {
            throw new IllegalArgumentException("Pedido de serviço deve conter objeto Servico.");
        }

        servicoService.gerarCodigoSeNaoExistir(servico);

        if (servico.getEtapa() != null) {
            Etapa etapa = etapaService.buscarPorTipoAndEtapa(
                    "PEDIDO",
                    servico.getEtapa().getNome()
            );
            servico.setEtapa(etapa);
        }

        servico.setPedido(pedido);
        pedido.setServico(servico);

        Status status = statusService.buscarPorTipoAndStatus(
                pedido.getStatus().getTipo(),
                pedido.getStatus().getNome()
        );
        pedido.setStatus(status);

        BigDecimal total = BigDecimal.valueOf(
                servico.getPrecoBase() != null ? servico.getPrecoBase() : 0.0
        );
        pedido.setValorTotal(total);

        return pedido;
    }

    @Override
    public Pedido editar(Pedido origem, Pedido destino) {

        Servico antigo = origem.getServico();
        Servico novo = destino.getServico();

        if (novo == null) {
            throw new IllegalArgumentException("Pedido de serviço deve conter objeto Servico.");
        }

        antigo.setNome(novo.getNome());
        antigo.setDescricao(novo.getDescricao());
        antigo.setPrecoBase(novo.getPrecoBase());
        antigo.setAtivo(novo.getAtivo());

        if (novo.getEtapa() != null) {
            Etapa etapa = etapaService.buscarPorTipoAndEtapa(
                    "PEDIDO",
                    novo.getEtapa().getNome()
            );
            antigo.setEtapa(etapa);
        }

        Status status = statusService.buscarPorTipoAndStatus(
                destino.getStatus().getTipo(),
                destino.getStatus().getNome()
        );
        origem.setStatus(status);

        BigDecimal total = BigDecimal.valueOf(antigo.getPrecoBase());
        origem.setValorTotal(total);

        antigo.setPedido(origem);
        origem.setServico(antigo);

        return origem;
    }

    @Override
    public Pedido deletar(Pedido pedido) {

        if (pedido.getServico() != null) {
            pedido.getServico().setPedido(null);
        }

        return pedido;
    }
}
