package com.project.extension.controller.pedido.servico;

import com.project.extension.controller.pedido.servico.dto.PedidoMapper;
import com.project.extension.controller.pedido.servico.dto.PedidoRequestDto;
import com.project.extension.controller.pedido.servico.dto.PedidoResponseDto;
import com.project.extension.entity.Pedido;
import com.project.extension.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
public class PedidoControllerImpl implements PedidoControllerDoc{
    private final PedidoService service;
    private final PedidoMapper mapper;


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
    public ResponseEntity<Page<PedidoResponseDto>> buscarTodos(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Page<PedidoResponseDto>> buscarPorTipoAndEtapa(
            String nome,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPedidosPorTipoENomeDaEtapa(nome, pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<PedidoResponseDto> atualizar(PedidoRequestDto request, Integer id) {
        Pedido pedidoAtualizar = mapper.toEntity(request);
        Pedido pedidoAtualizado = service.editar(id, pedidoAtualizar);
        return ResponseEntity.status(200).body(mapper.toResponse(pedidoAtualizado));
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Pedido e vínculos removidos com sucesso.");
    }

    @Override
    public ResponseEntity<Page<PedidoResponseDto>> buscarPedidosDeServico(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPedidosDeServico(pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Page<PedidoResponseDto>> buscarPedidosDeProduto(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listarPedidosDeProduto(pageable).map(mapper::toResponse));
    }
}
