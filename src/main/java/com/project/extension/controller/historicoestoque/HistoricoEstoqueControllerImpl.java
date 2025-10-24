package com.project.extension.controller.historicoestoque;

import com.project.extension.dto.historicoestoque.HistoricoEstoqueMapper;
import com.project.extension.dto.historicoestoque.HistoricoEstoqueResponseDto;
import com.project.extension.entity.HistoricoEstoque;
import com.project.extension.service.HistoricoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/historicos-estoques")
@RequiredArgsConstructor
public class HistoricoEstoqueControllerImpl implements HistoricoEstoqueControllerDoc{

    private final HistoricoEstoqueService service;
    private final HistoricoEstoqueMapper mapper;

    @Override
    public ResponseEntity<List<HistoricoEstoqueResponseDto>> listar() {
        List<HistoricoEstoque> historicoEstoques = service.listar();

        return historicoEstoques.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(historicoEstoques.stream()
                    .map(mapper::toResponse)
                    .toList());
    }

    @Override
    public ResponseEntity<HistoricoEstoqueResponseDto> buscarPorId(Integer id) {
       HistoricoEstoque historicoEstoque = service.buscarPorId(id);
       return ResponseEntity.status(200).body(mapper.toResponse(historicoEstoque));
    }
}
