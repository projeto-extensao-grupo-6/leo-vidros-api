package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class PedidoNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Pedido não encontrado";

    public PedidoNaoEncontradoException() {
        super(MENSAGEM);
    }
}
