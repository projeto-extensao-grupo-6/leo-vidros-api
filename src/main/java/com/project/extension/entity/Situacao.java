package com.project.extension.entity;

public enum Situacao {
    DISPONIVEL(1),
    EM_CORTE(2),
    INDISPONIVEL(3);
    private final Integer situacao;

    Situacao(Integer situacao) {
        this.situacao = situacao;
    }
}
