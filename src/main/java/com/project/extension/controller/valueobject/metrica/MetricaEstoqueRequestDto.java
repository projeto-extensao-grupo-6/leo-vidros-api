package com.project.extension.controller.valueobject.metrica;

public record MetricaEstoqueRequestDto(
    Integer nivelMinimo,
    Integer nivelMaximo
) {
}
