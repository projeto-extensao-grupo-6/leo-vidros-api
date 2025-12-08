package com.project.extension.dto.agendamento;

import com.project.extension.dto.agendamentoproduto.AgendamentoProdutoRequestDto;
import com.project.extension.dto.endereco.EnderecoRequestDto;
import com.project.extension.dto.funcionario.FuncionarioRequestDto;
import com.project.extension.dto.funcionario.FuncionarioResponseDto;
import com.project.extension.dto.pedido.PedidoResponseDto;
import com.project.extension.dto.pedido.servico.ServicoRequestDto;
import com.project.extension.dto.pedido.servico.ServicoResponseDto;
import com.project.extension.dto.status.StatusRequestDto;
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
