package com.project.extension.dto.estoque;

import com.project.extension.entity.TipoMovimentacao;

public class MovimentacaoEstoqueDto {
    private Long fkEstoque;
    private Long fkUsuario;
    private TipoMovimentacao tipoMovimentacao;
    private Integer quantidade;
    private String observacao;

    public MovimentacaoEstoqueDto() {}

    // Getters e Setters
    public Long getFkEstoque() { return fkEstoque; }
    public void setFkEstoque(Long fkEstoque) { this.fkEstoque = fkEstoque; }
    public Long getFkUsuario() { return fkUsuario; }
    public void setFkUsuario(Long fkUsuario) { this.fkUsuario = fkUsuario; }
    public TipoMovimentacao getTipoMovimentacao() { return tipoMovimentacao; }
    public void setTipoMovimentacao(TipoMovimentacao tipoMovimentacao) { this.tipoMovimentacao = tipoMovimentacao; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public String getObservacao() { return observacao; }
    public void setObservacao(String observacao) { this.observacao = observacao; }
}
