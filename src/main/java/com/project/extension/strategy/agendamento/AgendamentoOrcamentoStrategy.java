package com.project.extension.strategy.agendamento;

import com.project.extension.entity.*;
import com.project.extension.service.EnderecoService;
import com.project.extension.service.EtapaService;
import com.project.extension.service.PedidoService;
import com.project.extension.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component("ORCAMENTO")
@AllArgsConstructor
public class AgendamentoOrcamentoStrategy implements AgendamentoStrategy {

    private final EtapaService etapaService;
    private final StatusService statusService;
    private final PedidoService pedidoService;
    private final EnderecoService enderecoService;

    @Override
    public Agendamento agendar(Agendamento agendamento) {
        if (agendamento.getTipoAgendamento() == null) {
            throw new IllegalArgumentException("Tipo do agendamento é obrigatório");
        }
        if (agendamento.getDataAgendamento() == null) {
            agendamento.setDataAgendamento(LocalDateTime.now());
        }
        if (agendamento.getObservacao() == null) {
            agendamento.setObservacao("");
        }

        if (agendamento.getPedido() != null) {
            Pedido pedidoSalvo = pedidoService.buscarPorId(agendamento.getPedido().getId());
            if (pedidoSalvo == null) {
                throw new IllegalArgumentException("Pedido não encontrado no banco");
            }

            Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", "AGUARDANDO ORÇAMENTO");
            if (etapa == null) {
                etapa = etapaService.cadastrar(new Etapa("PEDIDO", "AGUARDANDO ORÇAMENTO"));
            }

            pedidoSalvo.setEtapa(etapa);
            pedidoService.editar(pedidoSalvo, pedidoSalvo.getId());

            agendamento.setPedido(pedidoSalvo);
        }

        if (agendamento.getStatusAgendamento() != null) {
            Status statusAg = statusService.buscarPorTipoAndStatus(
                    agendamento.getStatusAgendamento().getTipo(),
                    agendamento.getStatusAgendamento().getNome()
            );

            if (statusAg == null) {
                statusAg = statusService.cadastrar(
                        new Status(
                                agendamento.getStatusAgendamento().getTipo(),
                                agendamento.getStatusAgendamento().getNome()
                        )
                );
            }

            agendamento.setStatusAgendamento(statusAg);
        }

        if (agendamento.getEndereco() != null) {
            Endereco enderecoSalvo = enderecoService.buscarPorCep(agendamento.getEndereco().getCep());
            if (enderecoSalvo == null) {
                enderecoSalvo = enderecoService.cadastrar(agendamento.getEndereco());
            }
            agendamento.setEndereco(enderecoSalvo);
        }

        agendamento.setFuncionarios(new ArrayList<>());
        agendamento.setAgendamentoProdutos(new ArrayList<>());

        return agendamento;
    }
}
