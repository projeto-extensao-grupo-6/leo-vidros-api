package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    PENDENTE(1),
    APROVADO(2),
    REJEITADO(3);

    private Integer id;
}
