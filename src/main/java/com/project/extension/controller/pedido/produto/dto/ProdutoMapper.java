package com.project.extension.controller.pedido.produto.dto;

import com.project.extension.controller.valueobject.atributo.AtributoProdutoMapper;
import com.project.extension.controller.valueobject.metrica.MetricaEstoqueMapper;
import com.project.extension.entity.Produto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ProdutoMapper {

    private final AtributoProdutoMapper atributoProdutoMapper;
    private final MetricaEstoqueMapper metricaEstoqueMapper;

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
        produto.setMetricaEstoque(metricaEstoqueMapper.toEntity(dto.metrica()));

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
                metricaEstoqueMapper.toResponse(produto.getMetricaEstoque()),
                atributoProdutoMapper.toResponse(produto.getAtributos())
        );
    }
}
