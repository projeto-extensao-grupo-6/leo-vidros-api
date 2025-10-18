package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class StatusNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Status não encontrado";

    public StatusNaoEncontradoException() {
        super(MENSAGEM);
    }
}
