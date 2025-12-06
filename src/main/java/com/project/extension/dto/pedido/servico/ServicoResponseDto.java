package com.project.extension.dto.pedido.servico;

import com.project.extension.dto.etapa.EtapaResponseDto;
import com.project.extension.dto.pedido.servico.agendamento.AgendamentoServicoResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public record ServicoResponseDto(
        Integer id,
        String codigo,
        String nome,
        String descricao,
        Double precoBase,
        Boolean ativo,
        LocalDateTime createdAt,
        EtapaResponseDto etapa,
        List<AgendamentoServicoResponseDto> agendamentos
) {}

