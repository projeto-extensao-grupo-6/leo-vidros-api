package com.project.extension.dto.funcionario;

import com.project.extension.entity.Funcionario;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioRequestDto dto) {
        if (dto == null) return null;

        return new Funcionario(
                dto.nome(),
                dto.telefone(),
                dto.funcao(),
                dto.contrato(),
                dto.ativo()
        );
    }

    public List<Funcionario> toEntity(List<FuncionarioRequestDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return Collections.emptyList();
        }

        return dtos.stream()
                .map(dto -> new Funcionario(
                        dto.nome(),
                        dto.telefone(),
                        dto.funcao(),
                        dto.contrato(),
                        dto.ativo() != null ? dto.ativo() : true
                ))
                .toList();
    }

    public FuncionarioResponseDto toResponse(Funcionario funcionario) {
        if (funcionario == null) return null;

        return new FuncionarioResponseDto(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getTelefone(),
                funcionario.getFuncao(),
                funcionario.getContrato(),
                funcionario.getAtivo()
        );
    }

    public List<FuncionarioResponseDto> toResponse(List<Funcionario> funcionarios) {
        if (funcionarios == null || funcionarios.isEmpty()) {
            return Collections.emptyList();
        }

        return funcionarios.stream()
                .map(funcionario -> new FuncionarioResponseDto(
                        funcionario.getId(),
                        funcionario.getNome(),
                        funcionario.getTelefone(),
                        funcionario.getFuncao(),
                        funcionario.getContrato(),
                        funcionario.getAtivo()
                ))
                .toList();
    }
}
