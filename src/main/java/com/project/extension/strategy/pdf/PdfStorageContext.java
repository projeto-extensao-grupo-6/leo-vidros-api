package com.project.extension.strategy.pdf;

import com.project.extension.rabbitmq.queue.PdfResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PdfStorageContext {

    private final PdfStorageStrategy strategy;

    public void armazenar(PdfResponse response) {
        strategy.armazenar(response);
    }

    public byte[] obterPorNumeroOrcamento(String numeroOrcamento) {
        return strategy.obterPorNumeroOrcamento(numeroOrcamento);
    }
}
