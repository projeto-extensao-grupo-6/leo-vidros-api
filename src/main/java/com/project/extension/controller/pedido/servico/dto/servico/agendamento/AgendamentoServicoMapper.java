package com.project.extension.controller.pedido.servico.dto.servico.agendamento;

import com.project.extension.controller.valueobject.agendamentoproduto.AgendamentoProdutoMapper;
import com.project.extension.controller.valueobject.endereco.EnderecoMapper;
import com.project.extension.controller.funcionario.dto.FuncionarioMapper;
import com.project.extension.controller.valueobject.status.StatusMapper;
import com.project.extension.entity.Agendamento;
import com.project.extension.entity.AgendamentoProduto;
import com.project.extension.entity.TipoAgendamento;
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

    public Agendamento toEntity(AgendamentoServicoRequestDto dto) {
        if (dto == null) return null;

        Agendamento agendamento = new Agendamento(
                dto.tipoAgendamento(),
                dto.dataAgendamento(),
                dto.inicioAgendamento(),
                dto.fimAgendamento(),
                dto.observacao()
        );

        agendamento.setStatusAgendamento(statusMapper.toEntity(dto.statusAgendamento()));

        return agendamento;
    }

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
                (agendamento.getTipoAgendamento() == TipoAgendamento.ORCAMENTO
                        ? agendamento.getAgendamentoProdutos()
                        : java.util.List.<AgendamentoProduto>of()).stream()
                        .map(agendamentoProdutoMapper::toResponse)
                        .collect(Collectors.toList())
        );
    }
}
