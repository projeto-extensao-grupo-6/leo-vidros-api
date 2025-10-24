package com.project.extension.controller.estoque;

import com.project.extension.dto.estoque.EstoqueMapper;
import com.project.extension.dto.estoque.EstoqueRequestDto;
import com.project.extension.dto.estoque.EstoqueResponseDto;
import com.project.extension.entity.Estoque;
import com.project.extension.service.EstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoques")
@RequiredArgsConstructor
public class EstoqueControllerImpl implements EstoqueControllerDoc {

    private final EstoqueMapper mapper;
    private final EstoqueService service;

    @Override
    public ResponseEntity<EstoqueResponseDto> entrada(@RequestBody EstoqueRequestDto dto) {
        Estoque estoqueSalvar = mapper.toEntity(dto);
        Estoque estoqueSalvo = service.entrada(estoqueSalvar);
        return ResponseEntity.status(201).body(mapper.toResponse(estoqueSalvo));
    }

    @Override
    public ResponseEntity<EstoqueResponseDto> saida(@RequestBody EstoqueRequestDto dto) {
        Estoque estoqueSaida = mapper.toEntity(dto);
        Estoque estoqueAtualizado = service.saida(estoqueSaida);
        return ResponseEntity.ok(mapper.toResponse(estoqueAtualizado));
    }

    @Override
    public ResponseEntity<EstoqueResponseDto> buscarPorId(@PathVariable Integer id) {
        Estoque estoque = service.buscarPorId(id);
        return ResponseEntity.ok(mapper.toResponse(estoque));
    }

    @Override
    public ResponseEntity<List<EstoqueResponseDto>> listar() {
        List<Estoque> estoques = service.listar();

        return estoques.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(estoques.stream()
                    .map(mapper::toResponse)
                    .toList());
    }
}
