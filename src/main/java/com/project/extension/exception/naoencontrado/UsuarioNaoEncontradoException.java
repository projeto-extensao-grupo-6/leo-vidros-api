package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class UsuarioNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Usuário não encontrado";

    public UsuarioNaoEncontradoException() {
        super(MENSAGEM);
    }
}
