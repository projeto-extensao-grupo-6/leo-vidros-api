package com.project.extension.controller.historicoestoque;

import com.project.extension.controller.historicoestoque.dto.HistoricoEstoqueMapper;
import com.project.extension.controller.historicoestoque.dto.HistoricoEstoqueResponseDto;
import com.project.extension.entity.HistoricoEstoque;
import com.project.extension.service.HistoricoEstoqueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/historicos-estoques")
@RequiredArgsConstructor
public class HistoricoEstoqueControllerImpl implements HistoricoEstoqueControllerDoc{

    private final HistoricoEstoqueService service;
    private final HistoricoEstoqueMapper mapper;

    @Override
    public ResponseEntity<Page<HistoricoEstoqueResponseDto>> listar(
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.listar(pageable).map(mapper::toResponse));
    }

    @Override
    public ResponseEntity<Page<HistoricoEstoqueResponseDto>> buscarPorId(
            Integer id,
            @PageableDefault(size = 20, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorEstoqueId(id, pageable).map(mapper::toResponse));
    }
}
