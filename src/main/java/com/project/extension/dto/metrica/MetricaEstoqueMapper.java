package com.project.extension.dto.metrica;

import com.project.extension.entity.MetricaEstoque;
import org.springframework.stereotype.Component;

@Component
public class MetricaEstoqueMapper {

    public MetricaEstoque toEntity(MetricaEstoqueRequestDto dto) {
        if (dto == null) return null;

        return new MetricaEstoque(
                dto.nivelMinimo(),
                dto.nivelMaximo()
        );
    }

    public MetricaEstoqueResponseDto toResponse(MetricaEstoque metricaEstoque) {
        if (metricaEstoque == null) return null;

        return new MetricaEstoqueResponseDto(
                metricaEstoque.getId(),
                metricaEstoque.getNivelMinimo(),
                metricaEstoque.getNivelMaximo()
        );
    }

    public MetricaEstoque toEntity(MetricaEstoqueResponseDto dto) {
        if (dto == null) return null;

        MetricaEstoque metricaEstoque = new MetricaEstoque(
                dto.nivelMinimo(),
                dto.nivelMaximo()
        );
        metricaEstoque.setId(dto.id());
        return metricaEstoque;
    }
}
