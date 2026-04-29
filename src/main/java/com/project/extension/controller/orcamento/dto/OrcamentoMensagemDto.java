package com.project.extension.controller.orcamento.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO que representa a mensagem enviada ao RabbitMQ para geração do PDF.
 * Contrato com o microserviço (OrcamentoDTO). Ao adicionar/remover campos,
 * verifique se o consumidor ignora campos desconhecidos ou atualize-o junto.
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
        String observacoes,
        List<ProdutoInstalacaoMsg> produtosInstalacao
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

    public record ProdutoInstalacaoMsg(
            String nome,
            BigDecimal quantidade
    ) {}
}
