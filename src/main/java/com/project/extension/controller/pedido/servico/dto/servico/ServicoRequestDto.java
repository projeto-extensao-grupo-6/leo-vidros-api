package com.project.extension.controller.pedido.servico.dto.servico;

import com.project.extension.controller.valueobject.etapa.EtapaRequestDto;

public record ServicoRequestDto(
        String nome,
        String descricao,
        Double precoBase,
        Boolean ativo,
        EtapaRequestDto etapa
) {
}
