package com.project.extension.dto.servico;

import com.project.extension.dto.endereco.EnderecoResponseDto;
import com.project.extension.entity.enums.TipoServico;
import com.project.extension.entity.enums.TipoVidro;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ServicoResponseDto(
         LocalDate data,
         LocalTime horario,
         TipoServico tipoServico,
         String descricao,
         TipoVidro tipoVidro,
         List<Integer> tipoMaterialAuxiliares,
         EnderecoResponseDto endereco
) {
}
