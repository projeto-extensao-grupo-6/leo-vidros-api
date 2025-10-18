package com.project.extension.dto.estoque;

import com.project.extension.entity.AtributoProduto;
import com.project.extension.entity.Estoque;
import com.project.extension.entity.Produto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class EstoqueMapper {
    public ProdutoResponseDto toProdutoResponseDto(Produto produto) {
        ProdutoResponseDto dto = new ProdutoResponseDto();
        dto.setId(produto.getId());
        dto.setNome(produto.getNome());
        dto.setDescricao(produto.getDescricao());
        dto.setAtivo(produto.getAtivo());

        dto.setAtributos(produto.getAtributos() != null ? produto.getAtributos().stream()
                .map(this::toAtributoDto)
                .collect(Collectors.toList()) : new ArrayList<>());

        dto.setEstoques(produto.getEstoque() != null ? produto.getEstoque().stream()
                .map(this::toEstoqueResponseDto)
                .collect(Collectors.toList()) : new ArrayList<>());

        return dto;
    }

    public AtributoProdutoDto toAtributoDto(AtributoProduto atributo) {
        AtributoProdutoDto dto = new AtributoProdutoDto();
        dto.setTipo(atributo.getTipo());
        dto.setValor(atributo.getValor());
        return dto;
    }

    public EstoqueResponseDto toEstoqueResponseDto(Estoque estoque) {
        EstoqueResponseDto dto = new EstoqueResponseDto();
        dto.setId(estoque.getId());
        dto.setQuantidade(estoque.getQuantidade());
        dto.setReservado(estoque.getReservado());
        dto.setLocalizacao(estoque.getLocalizacao());
        return dto;
    }
}
