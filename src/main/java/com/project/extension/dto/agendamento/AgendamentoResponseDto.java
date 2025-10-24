package com.project.extension.dto.agendamento;

import com.project.extension.dto.endereco.EnderecoResponseDto;
import com.project.extension.dto.funcionario.FuncionarioResponseDto;
import com.project.extension.dto.pedido.PedidoResponseDto;
import com.project.extension.dto.status.StatusResponseDto;
import com.project.extension.entity.TipoAgendamento;

import java.time.LocalDateTime;
import java.util.List;

public record AgendamentoResponseDto(
        Integer id,
        TipoAgendamento tipoAgendamento,
        LocalDateTime dataAgendamento,
        StatusResponseDto statusAgendamento,
        String observacao,
        PedidoResponseDto pedido,
        EnderecoResponseDto endereco,
        List<FuncionarioResponseDto> funcionarios
) {
}
