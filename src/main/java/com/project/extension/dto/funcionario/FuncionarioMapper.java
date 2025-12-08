package com.project.extension.dto.funcionario;

import com.project.extension.entity.Funcionario;
import org.springframework.stereotype.Component;

@Component
public class FuncionarioMapper {

    public Funcionario toEntity(FuncionarioRequestDto dto) {
        if (dto == null) return null;

        Funcionario funcionario = new Funcionario();

        funcionario.setNome(dto.nome());
        funcionario.setTelefone(dto.telefone());
        funcionario.setFuncao(dto.funcao());
        funcionario.setContrato(dto.contrato());
        funcionario.setEscala(dto.escala());
        funcionario.setAtivo(dto.status());

        return funcionario;
    }

    public FuncionarioResponseDto toResponse(Funcionario funcionario) {
        if (funcionario == null) return null;
        return new FuncionarioResponseDto(
                funcionario.getId(),
                funcionario.getNome(),
                funcionario.getTelefone(),
                funcionario.getFuncao(),
                funcionario.getContrato(),
                funcionario.getEscala(),
                funcionario.getAtivo()
        );
    }

    public Funcionario toEntity(FuncionarioResponseDto dto) {
        if (dto == null) return null;

        Funcionario funcionario = new Funcionario();

        funcionario.setId(dto.id());
        funcionario.setNome(dto.nome());
        funcionario.setTelefone(dto.telefone());
        funcionario.setFuncao(dto.funcao());
        funcionario.setContrato(dto.contrato());
        funcionario.setEscala(dto.escala());
        funcionario.setAtivo(dto.status());

        return funcionario;
    }

}
