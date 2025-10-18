package com.project.extension.entity;

public enum TipoServico {

    INSTALACAO_VIDROS_COMUNS(1),
    VIDROS_TEMPERADOS(2),
    VIDROS_LAMINADOS(3),
    ESPELHOS(4),
    GUARDA_CORPOS(5),
    FECHAMENTO_DE_AREAS(6),
    DIVISORIAS(7),
    COBERTURAS(8),
    VIDROS_DECORATIVOS(9),
    MANUTENCAO(10),
    VITRINES(11),
    ENVIDRACAMENTO_ACUSTICO(12),
    PORTAS_AUTOMATICAS(13),
    MOVEIS_COM_VIDRO(14),
    ESQUADRIAS(15);

    private final Integer tipo;

    public Integer getTipo() {
        return tipo;
    }

    TipoServico(Integer tipo) {
        this.tipo = tipo;
    }
}