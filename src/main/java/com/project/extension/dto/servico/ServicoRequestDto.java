package com.project.extension.dto.servico;

import com.project.extension.dto.endereco.EnderecoRequestDto;
import com.project.extension.entity.enums.TipoServico;
import com.project.extension.entity.enums.TipoVidro;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ServicoRequestDto(
        @NotNull LocalDate data,
        @NotNull LocalTime horario,
        @NotNull TipoServico tipoServico,
        @NotBlank String descricao,
        @NotNull Integer tipoVidro,
        @NotNull List<Integer> tipoMaterialAuxiliares,
        @NotNull EnderecoRequestDto endereco
) {
}
