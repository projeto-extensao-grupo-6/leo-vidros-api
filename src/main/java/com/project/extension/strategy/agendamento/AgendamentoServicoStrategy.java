package com.project.extension.strategy.agendamento;

import com.project.extension.entity.*;
import com.project.extension.service.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("SERVICO")
@AllArgsConstructor
@Slf4j
public class AgendamentoServicoStrategy implements AgendamentoStrategy {

    private final EnderecoService enderecoService;
    private final FuncionarioService funcionarioService;
    private final EstoqueService estoqueService;
    private final EtapaService etapaService;
    private final StatusService statusService;

    @Override
    public Agendamento agendar(Agendamento agendamento) {
        log.info("Agendamento de agendamento: {}", agendamento.getPedido().getEtapa());
        Pedido pedido = agendamento.getPedido();
        log.debug("pedido: {}", pedido);
        log.debug("etapa: {}", pedido.getEtapa().getNome());
        if (pedido == null || !"ORÇAMENTO APROVADO".equals(pedido.getEtapa().getNome())) {
            throw new IllegalStateException("Só é possível agendar serviço se o orçamento estiver aprovado.");
        }
        agendamento.setTipoAgendamento(TipoAgendamento.SERVICO);

        if (agendamento.getFuncionarios() != null && !agendamento.getFuncionarios().isEmpty()) {
            List<Funcionario> funcionariosSalvos = new ArrayList<>();

            for (Funcionario f : agendamento.getFuncionarios()) {
                Funcionario funcionarioSalvo = null;

                if (f.getId() != null) {
                    funcionarioSalvo = funcionarioService.buscarPorId(f.getId());
                } else if (f.getTelefone() != null) {
                    funcionarioSalvo = funcionarioService.buscarPorTelefone(f.getTelefone());
                }

                if (funcionarioSalvo == null) {
                    funcionarioSalvo = funcionarioService.cadastrar(f);
                }

                funcionariosSalvos.add(funcionarioSalvo);
            }

            agendamento.setFuncionarios(funcionariosSalvos);
        }

        if (agendamento.getAgendamentoProdutos() != null && !agendamento.getAgendamentoProdutos().isEmpty()) {
            for (AgendamentoProduto ap : agendamento.getAgendamentoProdutos()) {
                ap.setAgendamento(agendamento);
                estoqueService.reservarProduto(ap.getProduto(), ap.getQuantidadeReservada());
            }
        }

        if (agendamento.getEndereco() != null) {
            var enderecoSalvo = enderecoService.buscarPorCep(agendamento.getEndereco().getCep());
            if (enderecoSalvo == null) {
                enderecoSalvo = enderecoService.cadastrar(agendamento.getEndereco());
            }
            agendamento.setEndereco(enderecoSalvo);
        }

        if (agendamento.getStatusAgendamento() != null) {
            String tipo = agendamento.getStatusAgendamento().getTipo();
            String nome = agendamento.getStatusAgendamento().getNome();

            Status statusAgendamento = statusService.buscarPorTipoAndStatus(tipo, nome);
            if (statusAgendamento == null) {
                statusAgendamento = statusService.cadastrar(new Status(tipo, nome));
            }

            agendamento.setStatusAgendamento(statusAgendamento);
        }

        Etapa etapaPedido = etapaService.buscarPorTipoAndEtapa("PEDIDO", "SERVIÇO AGENDADO");
        if (etapaPedido == null) {
            etapaPedido = etapaService.cadastrar(new Etapa("PEDIDO", "SERVIÇO AGENDADO"));
        }
        pedido.setEtapa(etapaPedido);

        return agendamento;
    }
}