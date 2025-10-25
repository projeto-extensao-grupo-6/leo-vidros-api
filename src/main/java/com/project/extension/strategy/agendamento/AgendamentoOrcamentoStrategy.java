package com.project.extension.strategy.agendamento;

import com.project.extension.entity.Agendamento;
import com.project.extension.entity.Endereco;
import com.project.extension.entity.Pedido;
import com.project.extension.entity.Status;
import com.project.extension.service.EnderecoService;
import com.project.extension.service.PedidoService;
import com.project.extension.service.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component("ORCAMENTO")
@AllArgsConstructor
public class AgendamentoOrcamentoStrategy implements AgendamentoStrategy {

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

            Status status = statusService.buscarPorTipoAndStatus("PEDIDO", "AGUARDANDO ORÇAMENTO");
            if (status == null) {
                status = statusService.cadastrar(new Status("PEDIDO", "AGUARDANDO ORÇAMENTO"));
            }

            pedidoSalvo.setStatus(status);
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
