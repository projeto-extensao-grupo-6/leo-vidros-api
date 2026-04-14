package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusFila {
    PENDENTE("pendente"),
    ENVIADO("enviado"),
    PROCESSANDO("processando"),
    CONCLUIDO("concluído"),
    ERRO("erro");

    private final String descricao;
}
