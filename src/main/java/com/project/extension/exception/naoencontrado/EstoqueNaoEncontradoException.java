package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class EstoqueNaoEncontradoException extends NaoEncontradoException {

    private static final String MENSAGEM = "Estoque não encontrado";
    public EstoqueNaoEncontradoException() {
        super(MENSAGEM);
    }
}
