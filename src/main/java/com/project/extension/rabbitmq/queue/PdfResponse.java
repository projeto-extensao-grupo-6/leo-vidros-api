package com.project.extension.rabbitmq.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString(exclude = "pdfBytes")
@JsonDeserialize(using = PdfResponseDeserializer.class)
public class PdfResponse {

    @JsonProperty("orcamentoId")
    private Integer orcamentoId;

    @JsonProperty("numeroOrcamento")
    private String numeroOrcamento;

    @JsonProperty("pdfBytes")
    private byte[] pdfBytes;

    @JsonProperty("tamanho")
    private Long tamanho;

    @JsonProperty("geradoEm")
    private Long geradoEm;

    @JsonProperty("nomeArquivo")
    private String nomeArquivo;

    public PdfResponse(String numeroOrcamento, byte[] pdfBytes) {
        this.numeroOrcamento = numeroOrcamento;
        this.pdfBytes = pdfBytes;
        this.tamanho = pdfBytes != null ? (long) pdfBytes.length : 0;
        this.geradoEm = System.currentTimeMillis();
    }
}
