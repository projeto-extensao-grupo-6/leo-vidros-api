package com.project.extension.dto.estoque;

public class EstoqueResponseDto {
    private Long id;
    private Integer quantidade;
    private Integer reservado;
    private String localizacao;

    public EstoqueResponseDto() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public Integer getReservado() { return reservado; }
    public void setReservado(Integer reservado) { this.reservado = reservado; }
    public String getLocalizacao() { return localizacao; }
    public void setLocalizacao(String localizacao) { this.localizacao = localizacao; }
}
