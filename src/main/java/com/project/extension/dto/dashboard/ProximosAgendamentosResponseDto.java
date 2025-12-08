package com.project.extension.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record ProximosAgendamentosResponseDto(
        Integer idAgendamento,
        LocalDate dataAgendamento,
        LocalTime inicioAgendamento,
        LocalTime fimAgendamento,
        String agendamentoObservacao,
        BigDecimal valorTotal,
        String pedidoObservacao,
        Boolean ativo,
        String status
) {
}
