package com.project.extension.controller.orcamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record OrcamentoResponseDto(
        Integer id,
        Integer pedidoId,
        Integer clienteId,
        String clienteNome,
        String clienteEmail,
        String clienteTelefone,
        String statusNome,
        String numeroOrcamento,
        LocalDate dataOrcamento,
        String observacoes,
        String prazoInstalacao,
        String garantia,
        String formaPagamento,
        BigDecimal valorSubtotal,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,
        String pdfPath,
        String statusFila,
        Boolean ativo,
        LocalDateTime createdAt,
        List<OrcamentoItemResponseDto> itens
) {}
