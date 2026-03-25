package com.project.extension.controller.funcionario.dto;

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
