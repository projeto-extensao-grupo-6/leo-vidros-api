package com.project.extension.dto.pedido.servico.agendamento;

import com.project.extension.dto.agendamentoproduto.AgendamentoProdutoMapper;
import com.project.extension.dto.endereco.EnderecoMapper;
import com.project.extension.dto.funcionario.FuncionarioMapper;
import com.project.extension.dto.status.StatusMapper;
import com.project.extension.entity.Agendamento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AgendamentoServicoMapper {

    private final EnderecoMapper enderecoMapper;
    private final FuncionarioMapper funcionarioMapper;
    private final StatusMapper statusMapper;
    private final AgendamentoProdutoMapper agendamentoProdutoMapper;

    public AgendamentoServicoResponseDto toResponse(Agendamento agendamento) {
        if (agendamento == null) return null;
        return new AgendamentoServicoResponseDto(
                agendamento.getId(),
                agendamento.getTipoAgendamento(),
                agendamento.getDataAgendamento(),
                agendamento.getInicioAgendamento(),
                agendamento.getFimAgendamento(),
                agendamento.getObservacao(),
                statusMapper.toResponse(agendamento.getStatusAgendamento()),
                enderecoMapper.toResponse(agendamento.getEndereco()),
                agendamento.getFuncionarios()
                        .stream()
                        .map(funcionarioMapper::toResponse)
                        .collect(Collectors.toList()),
                agendamento.getAgendamentoProdutos().stream()
                        .map(agendamentoProdutoMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }
}
