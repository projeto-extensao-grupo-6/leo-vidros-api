package com.project.extension.dto.funcionario;

import com.project.extension.entity.TipoAgendamento;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendaFuncionarioResponseDto(
        Integer agendamentoId,
        LocalDate dataAgendamento,
        LocalTime inicioAgendamento,
        LocalTime fimAgendamento,
        String tipoAgendamento,
        String statusAgendamento,
        String clienteNome,
        String servicoNome,
        String servicoCodigo,
        String etapaServico
) {}
