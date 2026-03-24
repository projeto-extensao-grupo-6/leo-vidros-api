package com.project.extension.rabbitmq.queue;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.util.Base64;

@Slf4j
public class PdfResponseDeserializer extends JsonDeserializer<PdfResponse> {

    @Override
    public PdfResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        

        if (node.isTextual()) {
            String pdfBase64 = node.asText();
            byte[] pdfBytes = Base64.getDecoder().decode(pdfBase64);
            return new PdfResponse("unknown", pdfBytes);
        }

        String numeroOrcamento = "unknown";

        if (node.has("numeroOrcamento")) {
            numeroOrcamento = node.get("numeroOrcamento").asText();
        } else if (node.has("numero_orcamento")) {
            numeroOrcamento = node.get("numero_orcamento").asText();
        } else {
        }
        
        byte[] pdfBytes = null;
        if (node.has("pdfBytes")) {
            String pdfBase64 = node.get("pdfBytes").asText();
            try {
                pdfBytes = Base64.getDecoder().decode(pdfBase64);
            } catch (IllegalArgumentException e) {
            }
        } else {
        }
        
        PdfResponse response = new PdfResponse(numeroOrcamento, pdfBytes);

        if (node.has("orcamentoId")) {
            response.setOrcamentoId(node.get("orcamentoId").asInt());
        }
        
        if (node.has("tamanho")) {
            response.setTamanho(node.get("tamanho").asLong());
        }
        if (node.has("geradoEm")) {
            response.setGeradoEm(node.get("geradoEm").asLong());
        }
        
                response.getOrcamentoId(),
                response.getNumeroOrcamento(), 
                response.getPdfBytes() != null ? response.getPdfBytes().length : 0);
        
        return response;
    }
}
