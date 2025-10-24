package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class HistoricoEstoqueNaoEncontradoException extends NaoEncontradoException {

    private static final String MENSAGEM = "Histórico Estoque não encontrado";

    public HistoricoEstoqueNaoEncontradoException() {
        super(MENSAGEM);
    }
}
