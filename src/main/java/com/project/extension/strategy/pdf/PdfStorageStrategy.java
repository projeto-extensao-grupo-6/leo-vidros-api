package com.project.extension.strategy.pdf;

import com.project.extension.rabbitmq.queue.PdfResponse;

public interface PdfStorageStrategy {

    void armazenar(PdfResponse response);

    byte[] obterPorNumeroOrcamento(String numeroOrcamento);
}
