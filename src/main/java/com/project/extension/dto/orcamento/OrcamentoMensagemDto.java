package com.project.extension.dto.orcamento;

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
        Double valorSubtotal,
        Double valorDesconto,
        Double valorTotal,
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
            Double quantidade,
            Double precoUnitario,
            Double desconto,
            Double subtotal,
            String Observacao
    ) {}
}
