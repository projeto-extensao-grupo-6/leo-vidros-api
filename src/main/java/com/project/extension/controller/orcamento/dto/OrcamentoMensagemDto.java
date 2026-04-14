package com.project.extension.controller.orcamento.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO que representa a mensagem enviada ao RabbitMQ para geração do PDF.
 * Deve manter o mesmo contrato que o microserviço espera (OrcamentoDTO).
 */
public record OrcamentoMensagemDto(
        Long id,
        String numeroOrcamento,
        String dataOrcamento,
        ClienteMsg cliente,
        List<ItemMsg> itens,
        BigDecimal valorSubtotal,
        BigDecimal valorDesconto,
        BigDecimal valorTotal,
        String prazoInstalacao,
        String garantia,
        String formaPagamento,
        String observacoes
) {
    public record ClienteMsg(
            String nome,
            String email,
            String telefone
    ) {}

    public record ItemMsg(
            String descricao,
            BigDecimal quantidade,
            BigDecimal precoUnitario,
            BigDecimal desconto,
            BigDecimal subtotal,
            String observacao
    ) {}
}
