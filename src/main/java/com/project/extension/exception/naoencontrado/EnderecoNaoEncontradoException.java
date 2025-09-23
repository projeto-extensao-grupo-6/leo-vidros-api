package com.project.extension.exception.naoencontrado;

import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class EnderecoNaoEncontradoException extends NaoEncontradoException {
    private final static String MENSAGEMM = "Endereço não encontrado!";

    public EnderecoNaoEncontradoException() {
        super(MENSAGEMM);
    }
}
