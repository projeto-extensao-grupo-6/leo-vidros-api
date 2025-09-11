package com.project.extension.exception.naoencontrado;


import com.project.extension.exception.naoencontrado.base.NaoEncontradoException;

public class RoleNaoEncontradoException extends NaoEncontradoException {
    public static final String MESSAGE = "Role não encontrado";
    public RoleNaoEncontradoException() {
        super(MESSAGE);
    }
}
