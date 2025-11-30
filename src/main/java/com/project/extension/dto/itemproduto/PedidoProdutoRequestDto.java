package com.project.extension.dto.itemproduto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoProdutoRequestDto(
        @NotNull(message = "O ID do cliente não pode ser nulo.")
        Integer clienteId,

        String observacao,

        @NotNull(message = "A lista de itens não pode ser nula.")
        List<ItemProdutoRequestDto> itens
) {
}
