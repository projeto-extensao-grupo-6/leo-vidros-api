package com.project.extension.service;

import com.project.extension.entity.Metrica;
import com.project.extension.entity.Produto;
import com.project.extension.exception.naoencontrado.MetricaNaoEncontradaException;
import com.project.extension.repository.MetricaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class MetricaService {

    private final MetricaRepository repository;

    public Metrica cadastrar(Metrica metrica, Produto produto) {
        metrica.setProduto(produto);

        Metrica metricaSalvo = repository.save(metrica);
        log.info("Métrica de estoque salvo com sucesso!");
        return metricaSalvo;
    }


    public Metrica buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Métrica de estoque com ID " + id + " não encontrado");
            return new MetricaNaoEncontradaException();
        });
    }

    public List<Metrica> listar() {
        List<Metrica> metricas = repository.findAll();
        log.info("Total de métricas de estoque encontradas: " + metricas.size());
        return metricas;
    }

    public Metrica editar(Metrica origem, Integer id) {
        Metrica destino = this.buscarPorId(id);

        this.atualizarDadosBasicos(destino, origem);

        Metrica produtoAtualizado = repository.save(destino);
        log.info("Atributo Produto atualizado com sucesso!");
        return produtoAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Atributo Produto deletado com sucesso");
    }

    private void atualizarDadosBasicos(Metrica destino, Metrica origem) {
        destino.setNivelMinimo(origem.getNivelMinimo());
        destino.setNivelMaximo(origem.getNivelMaximo());
    }
}
