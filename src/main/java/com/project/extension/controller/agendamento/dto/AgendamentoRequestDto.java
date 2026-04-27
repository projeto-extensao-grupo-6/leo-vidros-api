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
        @Positive(message = "ID do serviço deve ser positivo")
        @NotNull(message = "ID do serviço é obrigatório") Integer servicoId,
        @NotNull(message = "Tipo do agendamento é obrigatório") TipoAgendamento tipoAgendamento,
        @FutureOrPresent LocalDate dataAgendamento,
        @NotNull(message = "Horário de início é obrigatório") LocalTime inicioAgendamento,
        @NotNull(message = "Horário de fim é obrigatório") LocalTime fimAgendamento,
        @Valid @NotNull(message = "Status do agendamento é obrigatório") StatusRequestDto statusAgendamento,
        String observacao,
        @Valid @NotNull(message = "Endereço é obrigatório") EnderecoRequestDto endereco,
        @NotNull(message = "Lista de funcionários é obrigatória") List<Integer> funcionariosIds,
        @Valid @NotNull(message = "Lista de produtos é obrigatória") List<AgendamentoProdutoRequestDto> produtos
) {
}
