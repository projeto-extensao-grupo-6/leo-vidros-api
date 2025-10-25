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
    private final StatusService statusService;

    @Override
    public Agendamento agendar(Agendamento agendamento) {

        Pedido pedido = agendamento.getPedido();
        if (pedido == null || !"ORCAMENTO APROVADO".equals(pedido.getStatus().getNome())) {
            throw new IllegalStateException("Só é possível agendar serviço se o orçamento estiver aprovado.");
        }
        agendamento.setTipoAgendamento(TipoAgendamento.SERVICO);
        log.info(agendamento.getTipoAgendamento().toString());

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

        Status statusServico = statusService.buscarPorTipoAndStatus("PEDIDO", "SERVIÇO AGENDADO");
        if (statusServico == null) {
            statusServico = statusService.cadastrar(new Status("PEDIDO", "SERVIÇO AGENDADO"));
        }
        pedido.setStatus(statusServico);

        return agendamento;
    }
}