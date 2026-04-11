package com.project.extension.controller.orcamento.dto;

public record AtualizarStatusRequestDto(
        String status,
        String pdfPath
) {}
