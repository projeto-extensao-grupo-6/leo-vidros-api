package com.project.extension.dto.agendamento;

import com.project.extension.dto.agendamentoproduto.AgendamentoProdutoResponseDto;
import com.project.extension.dto.endereco.EnderecoResponseDto;
import com.project.extension.dto.funcionario.FuncionarioResponseDto;
import com.project.extension.dto.pedido.PedidoResponseDto;
import com.project.extension.dto.status.StatusResponseDto;
import com.project.extension.entity.TipoAgendamento;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public record AgendamentoResponseDto(
        Integer id,
        TipoAgendamento tipoAgendamento,
        LocalDate dataAgendamento,
        LocalTime inicioAgendamento,
        LocalTime fimAgendamento,
        String observacao,
        StatusResponseDto statusAgendamento,
        PedidoResponseDto pedido,
        EnderecoResponseDto endereco,
        List<FuncionarioResponseDto> funcionarios,
        List<AgendamentoProdutoResponseDto> produtos
) {
}
