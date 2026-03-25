package com.project.extension.controller.estoque.dto;

import com.project.extension.controller.pedido.produto.dto.ProdutoMapper;
import com.project.extension.entity.Estoque;
import com.project.extension.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EstoqueMapper {

    private final ProdutoMapper produtoMapper;
    private final ProdutoService produtoService;

    public Estoque toEntity(EstoqueRequestDto dto) {
        if (dto == null) return null;

        Estoque estoque = new Estoque(
                dto.localizacao(),
                dto.quantidadeTotal()
        );

        estoque.setProduto(produtoService.buscarPorId(dto.produtoId()));

        return estoque;
    }


    public EstoqueResponseDto toResponse(Estoque estoque) {
        if (estoque == null) return null;

        EstoqueResponseDto responseDto = new EstoqueResponseDto(
                estoque.getId(),
                estoque.getQuantidadeTotal(),
                estoque.getQuantidadeDisponivel(),
                estoque.getReservado(),
                estoque.getLocalizacao(),
                produtoMapper.toResponse(estoque.getProduto())
        );

        return responseDto;
    }
}
