package com.project.extension.controller.funcionario;

import com.project.extension.dto.funcionario.FuncionarioMapper;
import com.project.extension.dto.funcionario.FuncionarioRequestDto;
import com.project.extension.dto.funcionario.FuncionarioResponseDto;
import com.project.extension.entity.Funcionario;
import com.project.extension.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioControllerImpls implements FuncionarioControllerDoc{

    private final FuncionarioService service;
    private final FuncionarioMapper mapper;

    @Override
    public ResponseEntity<FuncionarioResponseDto> salvar(FuncionarioRequestDto request) {
        Funcionario funcionarioSalvar = mapper.toEntity(request);
        Funcionario funcionarioSalvo = service.cadastrar(funcionarioSalvar);

        return ResponseEntity.status(201).body(mapper.toResponse(funcionarioSalvo));
    }

    @Override
    public ResponseEntity<FuncionarioResponseDto> buscarPorId(Integer id) {
        Funcionario funcionario = service.buscarPorId(id);
        return ResponseEntity.status(200).body(mapper.toResponse(funcionario));
    }

    @Override
    public ResponseEntity<List<FuncionarioResponseDto>> buscarTodos() {
        List<Funcionario> funcionarios = service.listar();

        return funcionarios.isEmpty()
                ? ResponseEntity.status(204).build()
                : ResponseEntity.status(200).body(funcionarios.stream()
                    .map(mapper::toResponse)
                    .toList());
    }

    @Override
    public ResponseEntity<FuncionarioResponseDto> atualizar(FuncionarioRequestDto request, Integer id) {
        Funcionario funcionarioAtualizar = mapper.toEntity(request);
        Funcionario funcionarioAtualizado = service.editar(funcionarioAtualizar, id);

        return ResponseEntity.status(200).body(mapper.toResponse(funcionarioAtualizado));
    }

    @Override
    public ResponseEntity<String> deletar(Integer id) {
        service.deletar(id);
        return ResponseEntity.ok("Funcionário deletado com sucesso.");
    }
}
