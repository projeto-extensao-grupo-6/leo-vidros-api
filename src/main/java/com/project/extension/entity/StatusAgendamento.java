package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusAgendamento {

    PENDENTE("pendente"),
    CONFIRMADO("confirmado"),
    CANCELADO("cancelado"),
    CONCLUIDO("concluído");

    private final String statusAgendamento;
}
