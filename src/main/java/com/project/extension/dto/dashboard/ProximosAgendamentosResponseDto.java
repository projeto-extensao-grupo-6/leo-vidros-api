package com.project.extension.dto.dashboard;

import com.project.extension.entity.Agendamento;

import java.math.BigDecimal;
import java.sql.Time;
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
        Integer numero,
        String complemento,
        String bairro,
        String cidade,
        String uf,
        String cep,
        String status
) {
}
