package com.project.extension.controller.pedido.produto;

import com.project.extension.controller.pedido.produto.dto.ProdutoMapper;
import com.project.extension.controller.pedido.produto.dto.ProdutoRequestDto;
import com.project.extension.controller.pedido.produto.dto.ProdutoResponseDto;
import com.project.extension.controller.pedido.produto.dto.ProdutoStatusRequestDto;
import com.project.extension.entity.Produto;
import com.project.extension.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        return ResponseEntity.status(201).body(mapper.toResponse(produtoSalvo));
    }

    @Override
    public ResponseEntity<ProdutoResponseDto> buscarPorId(Integer id) {
        Produto produto = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(produto));
    }

    @Override
    public ResponseEntity<Page<ProdutoResponseDto>> buscarTodos(
            @PageableDefault(size = 20, sort = "nome") Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable).map(mapper::toResponse));
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

    @Override
    public ResponseEntity<ProdutoResponseDto> standBy(Integer id, ProdutoStatusRequestDto dto) {
        Produto produto = service.atualizarStatus(id, dto.status());
        return ResponseEntity.status(200).body(mapper.toResponse(produto));
    }

}
