package com.project.extension.entity;

public enum TipoVidro {

    COMUM(1),
    TEMPERADO(2),
    LAMINADO(3),
    FUME(4),
    JATEADO(5),
    SERIGRAFADO(6),
    REFLETIVO(7),
    BLINDADO(8),
    COLORIDO(9),
    ACUSTICO(10);

    private final Integer tipoVidro;

    TipoVidro(Integer tipoVidro) {
        this.tipoVidro = tipoVidro;
    }

    public Integer getTipoVidro() {
        return tipoVidro;
    }
}
