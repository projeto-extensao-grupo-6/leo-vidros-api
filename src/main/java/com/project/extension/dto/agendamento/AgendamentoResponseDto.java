package com.project.extension.dto.agendamento;

import com.project.extension.entity.StatusAgendamento;
import com.project.extension.entity.TipoAgendamento;

import java.time.LocalDateTime;

public record AgendamentoResponseDto(
        Integer id,
        TipoAgendamento tipoAgendamento,
        LocalDateTime dataAgendamento,
        StatusAgendamento statusAgendamento,
        String observacao
) {
}
