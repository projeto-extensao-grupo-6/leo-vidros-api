    package com.project.extension.controller.agendamento.dto;

import com.project.extension.controller.valueobject.agendamentoproduto.AgendamentoProdutoMapper;
import com.project.extension.controller.valueobject.endereco.EnderecoMapper;
import com.project.extension.controller.funcionario.dto.FuncionarioMapper;
import com.project.extension.controller.pedido.servico.dto.servico.ServicoMapper;
import com.project.extension.controller.valueobject.status.StatusMapper;
import com.project.extension.entity.Agendamento;
import com.project.extension.entity.AgendamentoProduto;
import com.project.extension.entity.Funcionario;
import com.project.extension.entity.Servico;
import com.project.extension.entity.TipoAgendamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AgendamentoMapper {

    private final EnderecoMapper enderecoMapper;
    private final FuncionarioMapper funcionarioMapper;
    private final StatusMapper statusMapper;
    private final AgendamentoProdutoMapper agendamentoProdutoMapper;
    private final ServicoMapper servicoMapper;

    public Agendamento toEntity(AgendamentoRequestDto dto) {
        if (dto == null) return null;

        Agendamento agendamento = new Agendamento(
                dto.tipoAgendamento(),
                dto.dataAgendamento(),
                dto.inicioAgendamento(),
                dto.fimAgendamento(),
                dto.observacao()
        );

        agendamento.setEndereco(enderecoMapper.toEntity(dto.endereco()));
        agendamento.setStatusAgendamento(statusMapper.toEntity(dto.statusAgendamento()));

        // Create a Servico stub with just the ID
        Servico servicoStub = new Servico();
        servicoStub.setId(dto.servicoId());
        agendamento.setServico(servicoStub);

        List<Funcionario> funcionarios = dto.funcionariosIds().stream()
                .map(id -> { Funcionario f = new Funcionario(); f.setId(id); return f; })
                .collect(Collectors.toList());
        agendamento.setFuncionarios(funcionarios);

        List<AgendamentoProduto> agendamentoProdutos = dto.tipoAgendamento() == TipoAgendamento.ORCAMENTO
                ? dto.produtos().stream()
                .map(agendamentoProdutoMapper::toEntity)
                .collect(Collectors.toList())
                : List.of();
        agendamento.setAgendamentoProdutos(agendamentoProdutos);

        return agendamento;
    }

    public AgendamentoResponseDto toResponse(Agendamento agendamento) {
        if (agendamento == null) return null;

        return new AgendamentoResponseDto(
                agendamento.getId(),
                agendamento.getTipoAgendamento(),
                agendamento.getDataAgendamento(),
                agendamento.getInicioAgendamento(),
                agendamento.getFimAgendamento(),
                agendamento.getObservacao(),
                statusMapper.toResponse(agendamento.getStatusAgendamento()),
                servicoMapper.toResponse(agendamento.getServico()),
                enderecoMapper.toResponse(agendamento.getEndereco()),
                agendamento.getFuncionarios()
                        .stream()
                        .map(funcionarioMapper::toResponse)
                        .collect(Collectors.toList()),
                (agendamento.getTipoAgendamento() == TipoAgendamento.ORCAMENTO
                        ? agendamento.getAgendamentoProdutos()
                        : List.<AgendamentoProduto>of()).stream()
                        .map(agendamentoProdutoMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }
}
