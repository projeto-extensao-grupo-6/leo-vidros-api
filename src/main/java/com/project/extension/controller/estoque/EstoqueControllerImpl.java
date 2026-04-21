package com.project.extension.controller.estoque;

import com.project.extension.controller.estoque.dto.EstoqueMapper;
import com.project.extension.controller.estoque.dto.EstoqueRequestDto;
import com.project.extension.controller.estoque.dto.EstoqueResponseDto;
import com.project.extension.entity.Estoque;
import com.project.extension.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estoques")
@RequiredArgsConstructor
public class EstoqueControllerImpl implements EstoqueControllerDoc {

    private final EstoqueMapper mapper;
    private final EstoqueService service;

    @Override
    public ResponseEntity<EstoqueResponseDto> entrada(@RequestBody EstoqueRequestDto dto) {
        Estoque estoqueSalvar = mapper.toEntity(dto);
        Estoque estoqueSalvo = service.entrada(estoqueSalvar, dto.observacao());
        return ResponseEntity.status(201).body(mapper.toResponse(estoqueSalvo));
    }

    @Override
    public ResponseEntity<EstoqueResponseDto> saida(@RequestBody EstoqueRequestDto dto) {
        Estoque estoqueSaida = mapper.toEntity(dto);
        Estoque estoqueAtualizado = service.saida(estoqueSaida, dto.observacao());
        return ResponseEntity.ok(mapper.toResponse(estoqueAtualizado));
    }

    @Override
    public ResponseEntity<EstoqueResponseDto> buscarPorId(@PathVariable Integer id) {
        Estoque estoque = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(estoque));
    }

    @Override
    public ResponseEntity<Page<EstoqueResponseDto>> listar(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable).map(mapper::toResponse));
    }
}
