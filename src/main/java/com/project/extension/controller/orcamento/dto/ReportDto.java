package com.project.extension.controller.orcamento.dto;

public record ReportDto(
        byte[] content,
        String mimeType,
        String fileName
) {
}
