package com.project.extension.dto.produto;

import com.project.extension.dto.atributo.AtributoProdutoMapper;
import com.project.extension.entity.Produto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProdutoMapper {

    private final AtributoProdutoMapper atributoProdutoMapper;

    public Produto toEntity(ProdutoRequestDto dto) {
        if (dto == null) return null;

        Produto produto = new Produto(
                dto.nome(),
                dto.descricao(),
                dto.unidademedida(),
                dto.preco(),
                dto.ativo()
        );

        produto.setAtributos(atributoProdutoMapper.toEntity(dto.atributos()));

        return produto;
    }

    public ProdutoResponseDto toResponse(Produto produto) {
        if (produto == null) return null;

        return new ProdutoResponseDto(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getUnidademedida(),
                produto.getPreco(),
                produto.getAtivo(),
                atributoProdutoMapper.toResponse(produto.getAtributos())
        );
    }
}
