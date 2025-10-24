package com.project.extension.exception.naopodesernegativo.base;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.METHOD_NOT_ALLOWED)
public class NaoPodeSerNegativoException extends RuntimeException {

    public NaoPodeSerNegativoException(String message) {
        super(message);
    }
}
