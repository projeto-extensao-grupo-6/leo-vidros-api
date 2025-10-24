package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoAgendamento {
    ORCAMENTO("orçamento"),
    SERVICO("serviço");

    private final String tipoAgendamento;
}
