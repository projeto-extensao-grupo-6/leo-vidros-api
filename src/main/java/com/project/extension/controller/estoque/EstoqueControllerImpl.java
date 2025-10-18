package com.project.extension.controller.estoque;

import com.project.extension.dto.estoque.EstoqueResponseDto;
import com.project.extension.dto.estoque.MovimentacaoEstoqueDto;
import com.project.extension.dto.estoque.ProdutoRequestDto;
import com.project.extension.dto.estoque.ProdutoResponseDto;
import com.project.extension.entity.Estoque;
import com.project.extension.service.EstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueControllerImpl {
    private final EstoqueService estoqueService;

    public EstoqueControllerImpl(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @PostMapping("/produtos")
    public ResponseEntity<ProdutoResponseDto> cadastrarProduto(@RequestBody ProdutoRequestDto dto)
    {
        ProdutoResponseDto novoProduto = estoqueService.cadastrarProduto(dto);
        return new ResponseEntity<>(novoProduto, HttpStatus.OK); // 201 - CREATED
    }

    @GetMapping("/produtos")
    public ResponseEntity<List<ProdutoResponseDto>> listarProdutos()
    {
        List<ProdutoResponseDto> produtos = estoqueService.listarTodosProdutos();
        return ResponseEntity.ok(produtos); // 200 - OK
    }

    @GetMapping("/produtos/{id}" )
    public ResponseEntity<ProdutoResponseDto> obterProdutoPorId(@PathVariable Long id)
    {
        ProdutoResponseDto produto = estoqueService.buscarProdutoPorId(id);
        return ResponseEntity.ok(produto); // 200 - OK
    }

    @PatchMapping("/movimentacao")
    public ResponseEntity<EstoqueResponseDto> movimentarEstoque(@RequestBody MovimentacaoEstoqueDto movimentacaoDto)
    {
        EstoqueResponseDto estoqueAtualizado = estoqueService.movimentarEstoque(movimentacaoDto);
        return ResponseEntity.ok(estoqueAtualizado); // 200 - OK
    }

    @DeleteMapping("/produtos/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable Long id)
    {
        estoqueService.deletarProduto(id);
        return ResponseEntity.noContent().build(); // 204 - NO CONTENT
    }
}