package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class ProdutoNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Produto não encontrado";

    public ProdutoNaoEncontradoException() {
        super(MENSAGEM);
    }
}
