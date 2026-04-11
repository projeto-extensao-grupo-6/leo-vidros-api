package com.project.extension.controller.orcamento.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record OrcamentoRequestDto(
        @NotNull(message = "O pedido é obrigatório")
        Integer pedidoId,

        Integer clienteId,

        String statusNome,

        String numeroOrcamento,

        @NotNull(message = "A data do orçamento é obrigatória")
        LocalDate dataOrcamento,

        String observacoes,
        String prazoInstalacao,
        String garantia,
        String formaPagamento,

        BigDecimal valorSubtotal,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,

        List<OrcamentoItemRequestDto> itens
) {}
