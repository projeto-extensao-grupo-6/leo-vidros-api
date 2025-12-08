package com.project.extension.dto.pedido.produto;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemPedidoRequestDto(
        @NotNull(message = "O ID do estoque não pode ser nulo.")
        Integer estoqueId,

        @NotNull(message = "A quantidade solicitada não pode ser nula.")
        BigDecimal quantidadeSolicitada,

        @NotNull(message = "O preço unitário não pode ser nulo.")
        BigDecimal precoUnitarioNegociado,

        String observacao
) {
}
