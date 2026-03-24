package com.project.extension.controller.orcamento.dto;

import com.project.extension.entity.Orcamento;
import com.project.extension.entity.OrcamentoItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrcamentoMapper {

    public OrcamentoResponseDto toResponse(Orcamento entity) {
        List<OrcamentoItemResponseDto> itensDto = entity.getItens() != null
                ? entity.getItens().stream().map(this::toItemResponse).toList()
                : List.of();

        return new OrcamentoResponseDto(
                entity.getId(),
                entity.getPedido() != null ? entity.getPedido().getId() : null,
                entity.getCliente() != null ? entity.getCliente().getId() : null,
                entity.getCliente() != null ? entity.getCliente().getNome() : null,
                entity.getCliente() != null ? entity.getCliente().getEmail() : null,
                entity.getCliente() != null ? entity.getCliente().getTelefone() : null,
                entity.getStatus() != null ? entity.getStatus().getNome() : null,
                entity.getNumeroOrcamento(),
                entity.getDataOrcamento(),
                entity.getObservacoes(),
                entity.getPrazoInstalacao(),
                entity.getGarantia(),
                entity.getFormaPagamento(),
                entity.getValorSubtotal(),
                entity.getValorDesconto(),
                entity.getValorTotal(),
                entity.getPdfPath(),
                entity.getAtivo(),
                entity.getCreatedAt(),
                itensDto
        );
    }

    public OrcamentoItemResponseDto toItemResponse(OrcamentoItem item) {
        return new OrcamentoItemResponseDto(
                item.getId(),
                item.getProduto() != null ? item.getProduto().getId() : null,
                item.getProduto() != null ? item.getProduto().getNome() : null,
                item.getDescricao(),
                item.getQuantidade(),
                item.getPrecoUnitario(),
                item.getDesconto(),
                item.getSubtotal(),
                item.getObservacao(),
                item.getOrdem()
        );
    }
}
