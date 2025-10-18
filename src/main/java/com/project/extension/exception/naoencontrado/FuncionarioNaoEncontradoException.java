package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class FuncionarioNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Funcionário não encontrado";

    public FuncionarioNaoEncontradoException() {
        super(MENSAGEM);
    }
}