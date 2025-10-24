package com.project.extension.dto.atributo;

import com.project.extension.entity.AtributoProduto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AtributoProdutoMapper {

    public AtributoProduto toEntity(AtributoProdutoRequestDto dto) {
        if (dto == null) return null;

        return new AtributoProduto(
                dto.tipo(),
                dto.valor()
        );
    }

    public List<AtributoProduto> toEntity(List<AtributoProdutoRequestDto> dtos) {
        if (dtos == null) return null;

        return dtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    public AtributoProdutoResponseDto toResponse(AtributoProduto atributoProduto) {
        if (atributoProduto == null) return null;

        return new AtributoProdutoResponseDto(
                atributoProduto.getId(),
                atributoProduto.getTipo(),
                atributoProduto.getValor()
        );
    }

    public List<AtributoProdutoResponseDto> toResponse(List<AtributoProduto> atributos) {
        if (atributos == null) return null;

        return atributos.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }
}
