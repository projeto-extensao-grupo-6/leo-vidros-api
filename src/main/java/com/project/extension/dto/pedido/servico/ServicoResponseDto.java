package com.project.extension.dto.servico;

import com.project.extension.dto.etapa.EtapaResponseDto;

import java.time.LocalDateTime;

public record ServicoResponseDto(
        Integer id,
        String codigo,
        String nome,
        String descricao,
        Double precoBase,
        Boolean ativo,
        LocalDateTime createdAt,
        EtapaResponseDto etapa
) {
}
