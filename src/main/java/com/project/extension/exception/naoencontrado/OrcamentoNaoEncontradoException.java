package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class OrcamentoNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Orçamento não encontrado.";

    public OrcamentoNaoEncontradoException() {
        super(MENSAGEM);
    }

    public OrcamentoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
