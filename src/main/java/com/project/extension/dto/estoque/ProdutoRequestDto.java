package com.project.extension.dto.estoque;

import java.util.List;

public class ProdutoRequestDto {
    private String nome;
    private String descricao;
    private Boolean ativo = true;
    private List<AtributoProdutoDto> atributos;
    private Integer quantidadeInicial;
    private String localizacaoInicial;

    public ProdutoRequestDto() {}

    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Boolean getAtivo() { return ativo; }
    public void setAtivo(Boolean ativo) { this.ativo = ativo; }
    public List<AtributoProdutoDto> getAtributos() { return atributos; }
    public void setAtributos(List<AtributoProdutoDto> atributos) { this.atributos = atributos; }
    public Integer getQuantidadeInicial() { return quantidadeInicial; }
    public void setQuantidadeInicial(Integer quantidadeInicial) { this.quantidadeInicial = quantidadeInicial; }
    public String getLocalizacaoInicial() { return localizacaoInicial; }
    public void setLocalizacaoInicial(String localizacaoInicial) { this.localizacaoInicial = localizacaoInicial; }
}
