package com.project.extension.rabbitmq.queue;

import com.project.extension.service.OrcamentoSseService;
import com.project.extension.strategy.pdf.PdfStorageContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PdfCacheService {

    private final PdfStorageContext storageContext;
    private final OrcamentoSseService sseService;

    @RabbitListener(queues = "fila.orcamento.pdf.resposta")
    public void receberPdf(PdfResponse response) {
        if (response == null) return;

        storageContext.armazenar(response);

        notificarFrontend(response);
    }

    public byte[] obterPorNumeroOrcamento(String numeroOrcamento) {
        return storageContext.obterPorNumeroOrcamento(numeroOrcamento);
    }

    private void notificarFrontend(PdfResponse response) {
        try {
            Integer orcamentoId = response.getOrcamentoId();
            String numero = response.getNumeroOrcamento();

            if (orcamentoId != null && orcamentoId > 0) {
                sseService.enviarEvento(orcamentoId, "FINALIZADO");
            } else {
                sseService.enviarEventoPorChave(numero, "FINALIZADO");
            }
        } catch (Exception e) {
            log.error("Falha ao notificar frontend via SSE", e);
        }
    }
}
