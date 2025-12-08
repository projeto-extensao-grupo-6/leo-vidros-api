package com.project.extension.entity;

import lombok.Getter;

@Getter
public enum OrigemMovimentacao {
    PEDIDO,
    SERVICO,
    AGENDAMENTO,
    PERDA,
    AJUSTE,
    MANUAL
}
