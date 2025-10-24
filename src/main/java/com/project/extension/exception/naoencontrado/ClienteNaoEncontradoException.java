package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class ClienteNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Cliente não encontrado";

    public ClienteNaoEncontradoException() {
        super(MENSAGEM);
    }
}
