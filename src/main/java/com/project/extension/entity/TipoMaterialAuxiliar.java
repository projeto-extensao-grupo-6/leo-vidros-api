package com.project.extension.entity;

public enum TipoMaterialAuxiliar {
    ALUMINIO(1),
    MADEIRA(2),
    ACO_INOX(3),
    RODIZIOS(4),
    TRILHOS(5),
    DOBRADIÇAS(6),
    PUXADORES(7),
    FECHADURAS(8);

    private final Integer tipoMaterialAuxiliar;

    TipoMaterialAuxiliar(Integer tipoMaterialAuxiliar) {
        this.tipoMaterialAuxiliar = tipoMaterialAuxiliar;
    }

    public Integer getTipoMaterialAuxiliar() {
        return tipoMaterialAuxiliar;
    }
}
