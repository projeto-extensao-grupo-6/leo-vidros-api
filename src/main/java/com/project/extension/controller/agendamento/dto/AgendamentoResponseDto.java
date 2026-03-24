package com.project.extension.controller.agendamento.dto;

import com.project.extension.controller.valueobject.agendamentoproduto.AgendamentoProdutoResponseDto;
import com.project.extension.controller.valueobject.endereco.EnderecoResponseDto;
import com.project.extension.controller.funcionario.dto.FuncionarioResponseDto;
import com.project.extension.controller.pedido.servico.dto.servico.ServicoResponseDto;
import com.project.extension.controller.valueobject.status.StatusResponseDto;
import com.project.extension.entity.TipoAgendamento;

import java.time.LocalDate;
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
        ServicoResponseDto servico,
        EnderecoResponseDto endereco,
        List<FuncionarioResponseDto> funcionarios,
        List<AgendamentoProdutoResponseDto> produtos
) {
}
