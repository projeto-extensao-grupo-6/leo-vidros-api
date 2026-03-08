package com.project.extension.dto.orcamento;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record OrcamentoRequestDto(
        @NotNull(message = "O pedido é obrigatório")
        Integer pedidoId,

        Integer clienteId,

        String statusNome,

        String numeroOrcamento,

        @NotNull(message = "A data do orçamento é obrigatória")
        String dataOrcamento,

        String observacoes,
        String prazoInstalacao,
        String garantia,
        String formaPagamento,

        BigDecimal valorSubtotal,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,

        List<OrcamentoItemRequestDto> itens
) {}
