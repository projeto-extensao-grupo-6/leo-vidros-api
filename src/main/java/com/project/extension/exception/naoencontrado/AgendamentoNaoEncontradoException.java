package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class AgendamentoNaoEncontradoException extends NaoEncontradoException {

    private static final String MENSAGEM = "Agendamento não encontrado";

    public AgendamentoNaoEncontradoException() {
        super(MENSAGEM);
    }

    public AgendamentoNaoEncontradoException(String message, Throwable cause) {
        super(message, cause);
    }
}


