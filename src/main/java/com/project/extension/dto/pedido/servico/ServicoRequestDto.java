package com.project.extension.dto.servico;

import com.project.extension.dto.etapa.EtapaRequestDto;

public record ServicoRequestDto(
        String nome,
        String descricao,
        Double precoBase,
        Boolean ativo,
        EtapaRequestDto etapa
) {
}
