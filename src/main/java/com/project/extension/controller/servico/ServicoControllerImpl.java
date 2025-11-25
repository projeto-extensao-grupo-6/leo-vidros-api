package com.project.extension.controller.servico;

import com.project.extension.dto.servico.ServicoMapper;
import com.project.extension.dto.servico.ServicoRequestDto;
import com.project.extension.dto.servico.ServicoResponseDto;
import com.project.extension.entity.Servico;
import com.project.extension.service.ServicoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/servicos")
@RequiredArgsConstructor
public class ServicoControllerImpl implements ServicoControllerDoc {

    private final ServicoService service;
    private final ServicoMapper mapper;


    @Override
    public ResponseEntity<ServicoResponseDto> cadastrar(ServicoRequestDto dto) {
        Servico servico = mapper.toEntity(dto);
        Servico servicoSalvo = service.cadastrar(servico);
        return ResponseEntity.status(201).body(mapper.toResponse(servicoSalvo));
    }

    @Override
    public ResponseEntity<List<ServicoResponseDto>> listar(String etapa) {
        List<Servico> servicos = service.listarPorEtapa(etapa);

        return servicos.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(servicos.stream()
                    .map(mapper::toResponse)
                    .toList());
    }

    @Override
    public ResponseEntity<ServicoResponseDto> buscarPorId(Integer id) {
        Servico servico = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(servico));
    }

    @Override
    public ResponseEntity<ServicoResponseDto> atualizar(Integer id, ServicoRequestDto dto) {
        Servico servico = mapper.toEntity(dto);
        Servico servicoAtualizado = service.editar(servico, id);
        return ResponseEntity.status(200).body(mapper.toResponse(servicoAtualizado));
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Serviço e vínculos removidos com sucesso.");
    }
}
