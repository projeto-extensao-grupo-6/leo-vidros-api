package com.project.extension.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MotivoPerda {
    QUEBRA(1),
    FURTO(2),
    VENCIMENTO(3),
    OUTRO(4);

    private final Integer motivoPerda;
}
