package com.project.extension.dto.estoque;

import java.util.List;

public class ProdutoResponseDto {
    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private List<AtributoProdutoDto> atributos;
    private List<EstoqueResponseDto> estoques;

    public ProdutoResponseDto() {}

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public List<AtributoProdutoDto> getAtributos() { return atributos; }
    public void setAtributos(List<AtributoProdutoDto> atributos) { this.atributos = atributos; }
    public List<EstoqueResponseDto> getEstoques() { return estoques; }
    public void setEstoques(List<EstoqueResponseDto> estoques) { this.estoques = estoques; }
}
