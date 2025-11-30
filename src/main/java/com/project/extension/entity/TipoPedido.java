package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPedido {
    PRODUTO("produto"),
    SERVICO("serviço");

    private final String tipoPedido;
}
