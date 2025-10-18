package com.project.extension.service;

import com.project.extension.dto.estoque.*;
import com.project.extension.entity.*;
import com.project.extension.repository.AtributoProdutoRepository;
import com.project.extension.repository.EstoqueRepository;
import com.project.extension.repository.HistoricoEstoqueRepository;
import com.project.extension.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EstoqueService {
    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;
    private final AtributoProdutoRepository atributoProdutoRepository;
    private final HistoricoEstoqueRepository historicoEstoqueRepository;
    private final EstoqueMapper estoqueMapper;

    public EstoqueService(EstoqueRepository estoqueRepository, ProdutoRepository produtoRepository, AtributoProdutoRepository atributoProdutoRepository, HistoricoEstoqueRepository historicoEstoqueRepository, EstoqueMapper estoqueMapper) {
        this.estoqueRepository = estoqueRepository;
        this.produtoRepository = produtoRepository;
        this.atributoProdutoRepository = atributoProdutoRepository;
        this.historicoEstoqueRepository = historicoEstoqueRepository;
        this.estoqueMapper = estoqueMapper;
    }

    // =========================================================================
    // CRUD: CREATE (Cadastrar Novo Produto)
    // =========================================================================
    @Transactional
    public ProdutoResponseDto cadastrarProduto(ProdutoRequestDto dto)
    {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setAtivo(dto.getAtivo() != null ? dto.getAtivo() : true);
        produto.setCreated_at(LocalDateTime.now());
        produto = produtoRepository.save(produto);

        final Produto produtoFinal = produto;

        if (dto.getAtributos() != null)
        {
            List<AtributoProduto> atributos = dto.getAtributos().stream()
                    .map(attrDto -> {
                        AtributoProduto atributo = new AtributoProduto();
                        atributo.setProduto(produtoFinal);
                        atributo.setTipo(attrDto.getTipo());
                        atributo.setValor(attrDto.getValor());
                        atributo.setCreatedAt(LocalDateTime.now());
                        return atributo;
                    }).collect(Collectors.toList());
            produto.setAtributos(atributos);
        }

        if (dto.getQuantidadeInicial() != null && dto.getQuantidadeInicial() > 0)
        {
            Estoque estoque = new Estoque();
            estoque.setProduto(produtoFinal);
            estoque.setQuantidade(dto.getQuantidadeInicial());
            estoque.setReservado(0);
            estoque.setLocalizacao(dto.getLocalizacaoInicial() != null ? dto.getLocalizacaoInicial() : "Padrão");
            estoque.setCreatedAt(LocalDateTime.now());
            estoque = estoqueRepository.save(estoque);

            HistoricoEstoque historico = new HistoricoEstoque();
            historico.setEstoque(estoque);
            historico.setTipoMovimentacao(TipoMovimentacao.ENTRADA);
            historico.setQuantidade(dto.getQuantidadeInicial());
            historico.setObservacao("Estoque inicial no cadastro do produto");
            historico.setDataMovimentacao(LocalDateTime.now());
            historicoEstoqueRepository.save(historico);

            produtoFinal.setEstoque(List.of(estoque));
        }

        return estoqueMapper.toProdutoResponseDto(produtoFinal);
    }

    // =========================================================================
    // CRUD: READ (Listar e Buscar)
    // =========================================================================
    public ProdutoResponseDto buscarProdutoPorId(Long id)
    {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        return estoqueMapper.toProdutoResponseDto(produto);
    }

    public List<ProdutoResponseDto> listarTodosProdutos()
    {
        return produtoRepository.findAll().stream()
                .map(estoqueMapper::toProdutoResponseDto)
                .collect(Collectors.toList());
    }

    // =========================================================================
    // CRUD: UPDATE (Movimentação de Estoque)
    // =========================================================================
    @Transactional
    public EstoqueResponseDto movimentarEstoque(MovimentacaoEstoqueDto dto)
    {
        Estoque estoque = estoqueRepository.findById(dto.getFkEstoque()).orElseThrow(() -> new RuntimeException("Local de estoque não encontrado"));

        int novaQuantidade = estoque.getQuantidade();

        if (dto.getTipoMovimentacao() == TipoMovimentacao.ENTRADA)
        {
            novaQuantidade += dto.getQuantidade();
        }
        else if (dto.getTipoMovimentacao() == TipoMovimentacao.SAIDA)
        {
            if (estoque.getQuantidade() < dto.getQuantidade())
            {
                throw new RuntimeException("Quantidade insuficiente em estoque para a saída");
            }
            novaQuantidade -= dto.getQuantidade();
        } else {
            throw new IllegalArgumentException("Tipo de movimentação inválido");
        }

        estoque.setQuantidade(novaQuantidade);
        estoque = estoqueRepository.save(estoque);

        HistoricoEstoque historico = new HistoricoEstoque();
        historico.setEstoque(estoque);
        historico.setTipoMovimentacao(dto.getTipoMovimentacao());
        historico.setQuantidade(dto.getQuantidade());
        historico.setObservacao(dto.getObservacao());
        historico.setDataMovimentacao(LocalDateTime.now());
        historicoEstoqueRepository.save(historico);

        return estoqueMapper.toEstoqueResponseDto(estoque);
    }

    // =========================================================================
    // CRUD: DELETE (Exclusão de Produto)
    // =========================================================================
    @Transactional
    public void deletarProduto(Long id)
    {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        List<Estoque> estoques = produto.getEstoque();
        for (Estoque estoque : estoques)
        {
            historicoEstoqueRepository.deleteByEstoque(estoque);
        }

        produtoRepository.delete(produto);
    }
}
