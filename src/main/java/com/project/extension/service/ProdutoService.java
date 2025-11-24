package com.project.extension.service;

import com.project.extension.entity.AtributoProduto;
import com.project.extension.entity.MetricaEstoque;
import com.project.extension.entity.Produto;
import com.project.extension.exception.naoencontrado.ProdutoNaoEncontradoException;
import com.project.extension.repository.ProdutoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ProdutoService {
    private final ProdutoRepository repository;
    private final AtributoProdutoService atributoProdutoService;
    private final MetricaEstoqueService metricaEstoqueService;
    private final LogService logService;

    public Produto cadastrar(Produto produto) {
        if (produto.getAtributos() != null) {
            for (AtributoProduto atributo : produto.getAtributos()) {
                atributo.setProduto(produto);
            }
        }

        if (produto.getMetricaEstoque() != null) {
            produto.setMetricaEstoque(metricaEstoqueService.cadastrar(produto.getMetricaEstoque()));
        }

        Produto produtoSalvo = repository.save(produto);
        String acao = produto.getId() == null ? "cadastrado" : "atualizado";
        String mensagem = String.format("Produto ID %d %s com sucesso. Nome: %s, Preço: %.2f.",
                produtoSalvo.getId(), acao, produtoSalvo.getNome(), produtoSalvo.getPreco());
        logService.success(mensagem);

        if (produto.getAtributos() != null) {
            for (AtributoProduto atributo : produto.getAtributos()) {
                atributoProdutoService.cadastrar(atributo, produtoSalvo);
            }
        }

        return produtoSalvo;
    }

    public Produto buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Produto com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.warn(mensagem);
            return new ProdutoNaoEncontradoException();
        });
    }

    public List<Produto> listar() {
        List<Produto> produtos = repository.findAll();
        logService.info(String.format("Busca por todos os produtos realizada. Total de registros: %d.", produtos.size()));
        return produtos;
    }

    public Produto editar(Produto origem, Integer id) {
        Produto destino = this.buscarPorId(id);

        this.atualizarDadosBasicos(destino, origem);
        this.atualizarAtributosProduto(destino, origem);
        this.atualizarMetricaEstoque(destino, origem);

        Produto produtoAtualizado = this.cadastrar(destino);
        log.info("Produto atualizado com sucesso!");
        return produtoAtualizado;
    }

    public void deletar(Integer id) {
        Produto produto = this.buscarPorId(id);

        if (produto.getAtributos() != null) {
            for (AtributoProduto atributo : produto.getAtributos()) {
                atributoProdutoService.deletar(atributo.getId());
            }
        }

        if (produto.getMetricaEstoque() != null) {
            metricaEstoqueService.deletar(produto.getMetricaEstoque().getId());
            produto.setMetricaEstoque(null);
        }

        repository.delete(produto);
        String mensagem = String.format("Produto ID %d (Nome: %s) deletado com sucesso, juntamente com seus atributos.",
                id, produto.getNome());
        logService.info(mensagem);
    }

    private void atualizarDadosBasicos(Produto destino, Produto origem) {
        destino.setNome(origem.getNome());
        destino.setDescricao(origem.getDescricao());
        destino.setUnidademedida(origem.getUnidademedida());
        destino.setPreco(origem.getPreco());
        destino.setAtivo(origem.getAtivo());
    }

    private void atualizarAtributosProduto(Produto produtoDestino, Produto produtoOrigem) {
        if (produtoOrigem.getAtributos() == null) return;

        if (produtoDestino.getAtributos() == null) {
            produtoDestino.setAtributos(new ArrayList<>());
        }

        Map<Integer, AtributoProduto> atributosAtuais = produtoDestino.getAtributos().stream()
                .filter(attr -> attr.getId() != null)
                .collect(Collectors.toMap(AtributoProduto::getId, attr -> attr));

        List<AtributoProduto> atributosAtualizados = new ArrayList<>();

        for (AtributoProduto atributoOrigem : produtoOrigem.getAtributos()) {
            atributoOrigem.setProduto(produtoDestino);

            if (atributoOrigem.getId() != null && atributosAtuais.containsKey(atributoOrigem.getId())) {
                AtributoProduto attrAtualizado = atributoProdutoService.editar(atributoOrigem, atributoOrigem.getId());
                atributosAtualizados.add(attrAtualizado);
                atributosAtuais.remove(atributoOrigem.getId());
            } else {
                AtributoProduto attrNovo = atributoProdutoService.cadastrar(atributoOrigem, produtoDestino);
                atributosAtualizados.add(attrNovo);
            }
        }

        for (AtributoProduto attrRemover : atributosAtuais.values()) {
            atributoProdutoService.deletar(attrRemover.getId());
        }
        produtoDestino.setAtributos(atributosAtualizados);
    }
    public void atualizarMetricaEstoque(Produto produtoDestino, Produto produtoOrigem) {
        if (produtoOrigem.getMetricaEstoque() == null) return;

        if (produtoDestino.getMetricaEstoque() != null &&
                produtoDestino.getMetricaEstoque().getId() != null) {

            Integer id = produtoDestino.getMetricaEstoque().getId();
            MetricaEstoque metricaAtualizada = metricaEstoqueService.editar(
                    produtoOrigem.getMetricaEstoque(),
                    id
            );

            produtoDestino.setMetricaEstoque(metricaAtualizada);
            return;
        }

        MetricaEstoque novaMetrica = metricaEstoqueService.cadastrar(
                produtoOrigem.getMetricaEstoque()
        );

        produtoDestino.setMetricaEstoque(novaMetrica);
    }

    public Produto atualizarStatus(Integer id, String status) {
        Produto produtoAtualizar = this.buscarPorId(id);

        produtoAtualizar.setAtivo(false);

        Produto produtoAtualizado = repository.save(produtoAtualizar);
        log.info("Status do Produto: {} atualizado com sucesso para: {}", produtoAtualizado.getNome(), status);
        return produtoAtualizado;
    }
}
