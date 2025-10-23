package com.project.extension.controller.produto;

import com.project.extension.dto.produto.ProdutoMapper;
import com.project.extension.dto.produto.ProdutoRequestDto;
import com.project.extension.dto.produto.ProdutoResponseDto;
import com.project.extension.entity.Produto;
import com.project.extension.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
public class ProdutoControllerImpl implements ProdutoControllerDoc{

    private final ProdutoService service;
    private final ProdutoMapper mapper;

    @Override
    public ResponseEntity<ProdutoResponseDto> salvar(ProdutoRequestDto request) {
        Produto produtoSalvar = mapper.toEntity(request);
        Produto produtoSalvo = service.cadastrar(produtoSalvar);
        return ResponseEntity.status(201).body(mapper.toResponse(produtoSalvar));
    }

    @Override
    public ResponseEntity<ProdutoResponseDto> buscarPorId(Integer id) {
        Produto produto = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(produto));
    }

    @Override
    public ResponseEntity<List<ProdutoResponseDto>> buscarTodos() {
        List<Produto> produtos = service.listar();
        return produtos.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(produtos.stream()
                    .map(mapper::toResponse)
                    .toList());
    }

    @Override
    public ResponseEntity<ProdutoResponseDto> atualizar(ProdutoRequestDto request, Integer id) {
        Produto produtoAtualizar = mapper.toEntity(request);
        Produto produtoAtualizado = service.editar(produtoAtualizar, id);
        return ResponseEntity.status(200).body(mapper.toResponse(produtoAtualizado));
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Produto e vínculos removidos com sucesso.");
    }
}
