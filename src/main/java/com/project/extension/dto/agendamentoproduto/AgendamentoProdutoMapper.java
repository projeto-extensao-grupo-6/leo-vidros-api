package com.project.extension.dto.agendamentoproduto;

import com.project.extension.dto.produto.ProdutoMapper;
import com.project.extension.entity.Agendamento;
import com.project.extension.entity.AgendamentoProduto;
import com.project.extension.entity.Produto;
import com.project.extension.service.AgendamentoService;
import com.project.extension.service.ProdutoService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AgendamentoProdutoMapper {

    private final AgendamentoService agendamentoService;
    private final ProdutoService produtoService;
    private final ProdutoMapper produtoMapper;

    public AgendamentoProduto toEntity(AgendamentoProdutoRequestDto dto) {
        if (dto == null) return null;

        AgendamentoProduto agendamentoProduto = new AgendamentoProduto(
                dto.quantidadeUtilizada(),
                dto.quantidadeReservada()
        );

        Produto produto = produtoService.buscarPorId(dto.produtoId());
        agendamentoProduto.setProduto(produto);

        return agendamentoProduto;
    }

    public AgendamentoProdutoResponseDto toResponse(AgendamentoProduto agendamentoProduto) {
        if (agendamentoProduto == null) return null;

        return new AgendamentoProdutoResponseDto(
                agendamentoProduto.getQuantidadeUtilizada(),
                agendamentoProduto.getQuantidadeReservada(),
                produtoMapper.toResponse(agendamentoProduto.getProduto())

        );
    }
}
