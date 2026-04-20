package com.project.extension.controller.agendamento.dto;

import com.project.extension.controller.valueobject.agendamentoproduto.AgendamentoProdutoRequestDto;
import com.project.extension.controller.valueobject.endereco.EnderecoRequestDto;
import com.project.extension.controller.valueobject.status.StatusRequestDto;
import com.project.extension.entity.TipoAgendamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoRequestDto(
        @Positive @NotNull Integer servicoId,
        @NotNull TipoAgendamento tipoAgendamento,
        @FutureOrPresent LocalDate dataAgendamento,
        @NotNull LocalTime inicioAgendamento,
        @NotNull LocalTime fimAgendamento,
        @Valid @NotNull StatusRequestDto statusAgendamento,
        String observacao,
        @Valid @NotNull EnderecoRequestDto endereco,
        @NotNull List<Integer> funcionariosIds,
        @Valid @NotNull List<AgendamentoProdutoRequestDto> produtos
) {
}
