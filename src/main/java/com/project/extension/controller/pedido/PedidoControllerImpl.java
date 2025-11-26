package com.project.extension.controller.pedido;

import com.project.extension.dto.itemproduto.PedidoProdutoMapper;
import com.project.extension.dto.itemproduto.PedidoProdutoRequestDto;
import com.project.extension.dto.itemproduto.PedidoProdutoResponseDto;
import com.project.extension.dto.pedido.PedidoMapper;
import com.project.extension.dto.pedido.PedidoRequestDto;
import com.project.extension.dto.pedido.PedidoResponseDto;
import com.project.extension.entity.Pedido;
import com.project.extension.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoControllerImpl implements PedidoControllerDoc{
    private final PedidoService service;
    private final PedidoMapper mapper;
    private final PedidoProdutoMapper produtoMapper;

    @Override
    public ResponseEntity<PedidoProdutoResponseDto> criarPedidoProduto(PedidoProdutoRequestDto request) {
        PedidoProdutoResponseDto response = service.criarPedidoProduto(request);
        return ResponseEntity.status(201).body(response);
    }

    @Override
    public ResponseEntity<PedidoResponseDto> salvar(PedidoRequestDto request) {
        Pedido pedidoSalvar = mapper.toEntity(request);
        Pedido pedidoSalvo = service.cadastrar(pedidoSalvar);
        return ResponseEntity.status(201).body(mapper.toResponse(pedidoSalvo));
    }

    @Override
    public ResponseEntity<PedidoResponseDto> buscarPorId(Integer id) {
        Pedido pedido = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(pedido));
    }

    @Override
    public ResponseEntity<List<PedidoResponseDto>> buscarTodos() {
       List<Pedido> pedidos = service.listar();

       return pedidos.isEmpty()
               ? ResponseEntity.status(204).build()
               : ResponseEntity.status(200).body(pedidos.stream()
                .map(mapper::toResponse)
                .toList());
    }

    @Override
    public ResponseEntity<PedidoResponseDto> atualizar(PedidoRequestDto request, Integer id) {
        Pedido pedidoAtualizar = mapper.toEntity(request);
        Pedido pedidoAtualizado = service.editar(pedidoAtualizar, id);
        return ResponseEntity.status(200).body(mapper.toResponse(pedidoAtualizado));
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Pedido e vínculos removidos com sucesso.");
    }
}
