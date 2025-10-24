package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class AtributoProdutoNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Atríbuto produto não encontrado";

    public AtributoProdutoNaoEncontradoException() {
        super(MENSAGEM);
    }
}

