package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class EtapaNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Etapa não encontrado";

    public EtapaNaoEncontradoException() {
        super(MENSAGEM);
    }
}
