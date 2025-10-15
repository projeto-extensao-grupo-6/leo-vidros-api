package com.project.extension.dto.agendamento;

import com.project.extension.entity.Agendamento;
import org.springframework.stereotype.Component;

@Component
public class AgendamentoMapper {

    public Agendamento toEntity(AgendamentoRequestDto dto) {
        if (dto == null) return null;

        return new Agendamento(
                 dto.tipoAgendamento(),
                 dto.dataAgendamento(),
                 dto.statusAgendamento(),
                 dto.observacao()
        );
    }

    public AgendamentoResponseDto toResponse(Agendamento agendamento) {
        if (agendamento == null) return null;

        return new AgendamentoResponseDto(
                agendamento.getId(),
                agendamento.getTipoAgendamento(),
                agendamento.getDataAgendamento(),
                agendamento.getStatusAgendamento(),
                agendamento.getObservacao()
        );
    }
}
