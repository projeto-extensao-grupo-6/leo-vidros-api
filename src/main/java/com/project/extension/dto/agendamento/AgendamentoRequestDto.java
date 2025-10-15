package com.project.extension.dto.agendamento;

import com.project.extension.entity.StatusAgendamento;
import com.project.extension.entity.TipoAgendamento;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;

public record AgendamentoRequestDto(
        @NotBlank TipoAgendamento tipoAgendamento,
        @FutureOrPresent LocalDateTime dataAgendamento,
        @NotBlank StatusAgendamento statusAgendamento,
        @NotBlank String observacao
) {
}
