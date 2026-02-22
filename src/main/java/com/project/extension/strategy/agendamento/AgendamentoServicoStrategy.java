package com.project.extension.strategy.agendamento;

import com.project.extension.entity.*;
import com.project.extension.exception.RegraNegocioException;
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
    private final ServicoService servicoService;

    @Override
    public Agendamento agendar(Agendamento agendamento) {
        Servico servico = agendamento.getServico();
        Servico servicoSalvo = servicoService.buscarPorId(servico.getId());
        if (servicoSalvo == null || !"ORÇAMENTO APROVADO".equals(servicoSalvo.getEtapa().getNome())) {
            throw new IllegalStateException("Só é possível agendar serviço se o orçamento estiver aprovado.");
        }
        agendamento.setTipoAgendamento(TipoAgendamento.SERVICO);

        if (agendamento.getFuncionarios() != null && !agendamento.getFuncionarios().isEmpty()) {
            if (agendamento.getInicioAgendamento() == null || agendamento.getFimAgendamento() == null) {
                throw new RegraNegocioException("Horário de início e fim são obrigatórios para validar a disponibilidade dos funcionários.");
            }

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

                boolean conflito = funcionarioService.temConflito(
                        funcionarioSalvo.getId(),
                        agendamento.getDataAgendamento(),
                        agendamento.getInicioAgendamento(),
                        agendamento.getFimAgendamento()
                );
                if (conflito) {
                    throw new RegraNegocioException(
                            String.format("Funcionário '%s' possui conflito de horário nesta data e horário.",
                                    funcionarioSalvo.getNome()));
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
        servicoSalvo.setEtapa(etapaPedido);
        servicoService.editar(servicoSalvo, servicoSalvo.getId());

        return agendamento;
    }
}