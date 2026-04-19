package com.project.extension.rabbitmq.queue;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonDeserialize(using = PdfResponseDeserializer.class)
public class PdfResponse implements Serializable {
    private static final long serialVersionUID = 1L;

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

    @Override
    public String toString() {
        return "PdfResponse{" +
                "numeroOrcamento='" + numeroOrcamento + '\'' +
                ", tamanho=" + tamanho +
                ", geradoEm=" + geradoEm +
                '}';
    }
}
