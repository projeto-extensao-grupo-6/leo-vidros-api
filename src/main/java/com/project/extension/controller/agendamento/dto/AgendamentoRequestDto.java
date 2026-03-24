package com.project.extension.controller.agendamento.dto;

import com.project.extension.controller.valueobject.agendamentoproduto.AgendamentoProdutoRequestDto;
import com.project.extension.controller.valueobject.endereco.EnderecoRequestDto;
import com.project.extension.controller.funcionario.dto.FuncionarioResponseDto;
import com.project.extension.controller.pedido.servico.dto.servico.ServicoResponseDto;
import com.project.extension.controller.valueobject.status.StatusRequestDto;
import com.project.extension.entity.TipoAgendamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoRequestDto(
        @Valid @NotNull ServicoResponseDto servico,
        @NotNull TipoAgendamento tipoAgendamento,
        @FutureOrPresent LocalDate dataAgendamento,
        @Valid @NotNull LocalTime inicioAgendamento,
        @Valid @NotNull LocalTime fimAgendamento,
        @Valid @NotNull StatusRequestDto statusAgendamento,
        @NotBlank String observacao,
        @Valid @NotNull EnderecoRequestDto endereco,
        @Valid @NotNull List<FuncionarioResponseDto> funcionarios,
        @Valid @NotNull List<AgendamentoProdutoRequestDto> produtos
) {
}
