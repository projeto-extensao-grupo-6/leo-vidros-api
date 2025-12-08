package com.project.extension.controller.funcionario;

import com.project.extension.dto.funcionario.FuncionarioMapper;
import com.project.extension.dto.funcionario.FuncionarioRequestDto;
import com.project.extension.dto.funcionario.FuncionarioResponseDto;

import com.project.extension.entity.Funcionario;
import com.project.extension.service.FuncionarioService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioControllerImpls implements FuncionarioControllerDoc {

    private final FuncionarioService service;
    private final FuncionarioMapper mapper;

    @Override
    @PostMapping
    public ResponseEntity<FuncionarioResponseDto> salvar(@RequestBody FuncionarioRequestDto request) {
        Funcionario funcionarioSalvar = mapper.toEntity(request);
        Funcionario funcionarioSalvo = service.cadastrar(funcionarioSalvar);
        return ResponseEntity.status(201).body(mapper.toResponse(funcionarioSalvo));
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(@PathVariable Integer id) {
        Funcionario funcionario = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(funcionario));
    }

    @Override
    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDto>> buscarTodos() {
        List<Funcionario> funcionarios = service.listar();

        if (funcionarios.isEmpty()) {
            return ResponseEntity.status(204).build();
        }

        return ResponseEntity.ok(
                funcionarios.stream()
                        .map(mapper::toResponse)
                        .toList()
        );
    }

    @Override
    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDto> atualizar(
            @RequestBody FuncionarioRequestDto request,
            @PathVariable Integer id) {

        Funcionario funcionarioAtualizar = mapper.toEntity(request);
        Funcionario funcionarioAtualizado = service.editar(funcionarioAtualizar, id);

        return ResponseEntity.status(200).body(mapper.toResponse(funcionarioAtualizado));
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Funcionário deletado com sucesso.");
    }
}
