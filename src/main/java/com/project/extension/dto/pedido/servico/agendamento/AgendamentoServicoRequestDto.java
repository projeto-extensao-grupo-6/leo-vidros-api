package com.project.extension.dto.pedido.servico.agendamento;

import com.project.extension.dto.status.StatusRequestDto;
import com.project.extension.entity.TipoAgendamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AgendamentoServicoRequestDto(
        @NotNull TipoAgendamento tipoAgendamento,
        @FutureOrPresent LocalDate dataAgendamento,
        @Valid @NotNull LocalTime inicioAgendamento,
        @Valid @NotNull LocalTime fimAgendamento,
        @Valid @NotNull StatusRequestDto statusAgendamento,
        @NotBlank String observacao
) {
}
