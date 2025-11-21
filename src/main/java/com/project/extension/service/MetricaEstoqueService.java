package com.project.extension.service;

import com.project.extension.entity.MetricaEstoque;
import com.project.extension.entity.Produto;
import com.project.extension.exception.naoencontrado.MetricaNaoEncontradaException;
import com.project.extension.repository.MetricaEstoqueRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MetricaEstoqueService {

    private final MetricaEstoqueRepository repository;

    public MetricaEstoque cadastrar(MetricaEstoque metricaEstoque) {
        MetricaEstoque metricaEstoqueSalvo = repository.save(metricaEstoque);
        log.info("Métrica de estoque salvo com sucesso!");
        return metricaEstoqueSalvo;
    }


    public MetricaEstoque buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Métrica de estoque com ID " + id + " não encontrado");
            return new MetricaNaoEncontradaException();
        });
    }

    public List<MetricaEstoque> listar() {
        List<MetricaEstoque> metricaEstoques = repository.findAll();
        log.info("Total de métricas de estoque encontradas: " + metricaEstoques.size());
        return metricaEstoques;
    }

    public MetricaEstoque editar(MetricaEstoque origem, Integer id) {
        MetricaEstoque destino = this.buscarPorId(id);

        this.atualizarDadosBasicos(destino, origem);

        MetricaEstoque produtoAtualizado = repository.save(destino);
        log.info("Atributo Produto atualizado com sucesso!");
        return produtoAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Atributo Produto deletado com sucesso");
    }

    private void atualizarDadosBasicos(MetricaEstoque destino, MetricaEstoque origem) {
        destino.setNivelMinimo(origem.getNivelMinimo());
        destino.setNivelMaximo(origem.getNivelMaximo());
    }
}
