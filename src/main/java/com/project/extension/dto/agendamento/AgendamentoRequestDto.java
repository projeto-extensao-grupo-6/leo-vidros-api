package com.project.extension.dto.agendamento;

import com.project.extension.dto.endereco.EnderecoRequestDto;
import com.project.extension.dto.funcionario.FuncionarioRequestDto;
import com.project.extension.dto.pedido.PedidoResponseDto;
import com.project.extension.dto.status.StatusRequestDto;
import com.project.extension.entity.TipoAgendamento;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoRequestDto(
        @NotBlank TipoAgendamento tipoAgendamento,
        @FutureOrPresent LocalDateTime dataAgendamento,
        @Valid StatusRequestDto statusAgendamento,
        @NotBlank String observacao,
        @Valid @NotNull EnderecoRequestDto endereco,
        @Valid @NotNull PedidoResponseDto pedido,
        @Valid@NotNull List<FuncionarioRequestDto> funcionarios
) {
}
