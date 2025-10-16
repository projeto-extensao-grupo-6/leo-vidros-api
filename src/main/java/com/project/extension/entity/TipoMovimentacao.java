package com.project.extension.entity;

public enum TipoMovimentacao {
    ENTRADA(1),
    SAIDA(2);

    private final Integer tipoMovimentacao;
    TipoMovimentacao(Integer tipoMovimentacao) {
        this.tipoMovimentacao = tipoMovimentacao;
    }

    public Integer getTipoMovimentacao() {
        return tipoMovimentacao;
    }
}
