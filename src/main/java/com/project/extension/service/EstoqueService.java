package com.project.extension.service;

import com.project.extension.entity.Estoque;
import com.project.extension.repository.EstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    public EstoqueService(EstoqueRepository estoqueRepository) {
        this.estoqueRepository = estoqueRepository;
    }

    public List<Estoque> listarProdutosEstoque()
    {
        return estoqueRepository.findAll();
    }

    public Optional<Estoque> buscarProdutoEstoquePorId(Long id)
    {
        return estoqueRepository.findById(id);
    }

    public Estoque salvarProdutosEstoque(Estoque estoque)
    {
        return estoqueRepository.save(estoque);
    }

    public Estoque atualizarProdutosEstoque(Long id, Estoque estoqueAtualizado)
    {
        return estoqueRepository.findById(id)
                .map(estoque -> {
                    estoque.setDataEntrada(estoqueAtualizado.getDataEntrada());
                    estoque.setNome(estoqueAtualizado.getNome());
                    estoque.setCategoria(estoqueAtualizado.getCategoria());
                    estoque.setEspessura(estoqueAtualizado.getEspessura());
                    estoque.setDimensao(estoqueAtualizado.getDimensao());
                    estoque.setQtdDisponivel(estoqueAtualizado.getQtdDisponivel());
                    estoque.setUnidadeMedida(estoqueAtualizado.getUnidadeMedida());
                    estoque.setStatus(estoqueAtualizado.getStatus());
                    estoque.setSituacao(estoqueAtualizado.getSituacao());
                    estoque.setTipoVidro(estoqueAtualizado.getTipoVidro());
                    estoque.setTipoMaterialAuxiliar(estoqueAtualizado.getTipoMaterialAuxiliar());
                    return estoqueRepository.save(estoque);
                }).orElseThrow(() -> new RuntimeException("Produto de estoque não encontrado com o ID: " + id));
    }

    public void deletarProdutoEstoque(Long id)
    {
        if (!estoqueRepository.existsById(id)) {
            throw new RuntimeException("Produto de estoque não encontrado com o ID: " + id);
        }
        estoqueRepository.deleteById(id);
    }

}
