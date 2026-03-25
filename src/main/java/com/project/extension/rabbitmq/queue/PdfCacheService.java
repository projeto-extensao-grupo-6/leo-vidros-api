package com.project.extension.rabbitmq.queue;

import com.project.extension.service.OrcamentoSseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class PdfCacheService {

    private final Map<String, byte[]> pdfCache = new ConcurrentHashMap<>();
    private final OrcamentoSseService sseService;
    
    @Value("${app.pdf.storage-path:resources/pdfs}")
    private String pdfStoragePath;

    public PdfCacheService(OrcamentoSseService sseService) {
        this.sseService = sseService;
    }

    @RabbitListener(queues = "fila.orcamento.pdf.resposta")
    public void receberPdf(PdfResponse response) {
        if (response == null) {
            return;
        }
        
        
        if (response.getPdfBytes() == null) {
            return;
        }

        String numeroOrcamento = response.getNumeroOrcamento();
        Integer orcamentoId = response.getOrcamentoId();
        byte[] pdfBytes = response.getPdfBytes();


        String chave = "orcamento_" + numeroOrcamento;
        pdfCache.put(chave, pdfBytes);


        try {
            salvarPdfEmArquivo(numeroOrcamento, pdfBytes);
        } catch (Exception e) {
        }

        try {
            if (orcamentoId != null && orcamentoId > 0) {
                sseService.enviarEvento(orcamentoId, "FINALIZADO");
            } else {
                sseService.enviarEventoPorChave(numeroOrcamento, "FINALIZADO");
            }
        } catch (Exception e) {
        }
    }

    public byte[] obterPdf(String chave) {
        return pdfCache.get(chave);
    }

    public void armazenarPdf(String numeroOrcamento, byte[] pdf) {
        pdfCache.put("orcamento_" + numeroOrcamento, pdf);
    }


    public byte[] obterPorNumeroOrcamento(String numeroOrcamento) {
        byte[] pdf = pdfCache.get("orcamento_" + numeroOrcamento);
        if (pdf != null) {
            return pdf;
        }

        try {
            byte[] pdfDoArquivo = lerPdfDoArquivo(numeroOrcamento);
            if (pdfDoArquivo != null) {
                // Armazenar em cache para próximas requisições
                pdfCache.put("orcamento_" + numeroOrcamento, pdfDoArquivo);
                return pdfDoArquivo;
            }
        } catch (Exception e) {
        }
        
        return null;
    }

    public void removerPdf(String chave) {
        pdfCache.remove(chave);
    }

    public void limparCache() {
        pdfCache.clear();
    }

    public int getTamanhoCacheSize() {
        return pdfCache.size();
    }

    public java.util.Set<String> obterChavesCache() {
        return new java.util.HashSet<>(pdfCache.keySet());
    }

    private void salvarPdfEmArquivo(String numeroOrcamento, byte[] pdfBytes) throws IOException {
        try {
            Path diretorio = Paths.get(pdfStoragePath);
            if (!Files.exists(diretorio)) {
                Files.createDirectories(diretorio);
            }

            String nomeArquivo = "orcamento_" + numeroOrcamento + ".pdf";
            Path caminhoArquivo = diretorio.resolve(nomeArquivo);

            try (FileOutputStream fos = new FileOutputStream(caminhoArquivo.toFile())) {
                fos.write(pdfBytes);
                fos.flush();
            }

        } catch (IOException e) {
            throw e;
        }
    }

    private byte[] lerPdfDoArquivo(String numeroOrcamento) throws IOException {
        try {
            String nomeArquivo = "orcamento_" + numeroOrcamento + ".pdf";
            Path caminhoArquivo = Paths.get(pdfStoragePath).resolve(nomeArquivo);

            if (!Files.exists(caminhoArquivo)) {
                return null;
            }

            byte[] pdfBytes = Files.readAllBytes(caminhoArquivo);
            return pdfBytes;
        } catch (IOException e) {
            throw e;
        }
    }
}
