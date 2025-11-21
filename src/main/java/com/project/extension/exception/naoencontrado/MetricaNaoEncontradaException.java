package com.project.extension.exception.naoencontrado;

public class MetricaNaoEncontradaException extends RuntimeException {
    private static final String MENSAGEM = "Etapa não encontrado";

    public MetricaNaoEncontradaException() {
        super(MENSAGEM);
    }
}
