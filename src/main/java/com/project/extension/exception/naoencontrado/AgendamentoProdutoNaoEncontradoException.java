package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class AgendamentoProdutoNaoEncontradoException extends NaoEncontradoException {
    private static final String MENSAGEM = "Agendamento produto não encontrado";
    public AgendamentoProdutoNaoEncontradoException() {
        super(MENSAGEM);
    }
}
