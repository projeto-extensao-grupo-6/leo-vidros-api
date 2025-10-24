package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoMovimentacao {
    ENTRADA(1),
    SAIDA(2);

    private final Integer tipoMovimentacao;
}
