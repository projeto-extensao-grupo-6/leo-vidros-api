package com.project.extension.strategy.pdf;

import com.project.extension.rabbitmq.queue.PdfResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.environment", havingValue = "production")
public class S3PdfStorageStrategy implements PdfStorageStrategy {

    private final S3Client s3Client;

    // Mapeia numeroOrcamento → nomeArquivo recebido da fila
    private final Map<String, String> arquivosS3 = new ConcurrentHashMap<>();

    @org.springframework.beans.factory.annotation.Value("${aws.s3.bucket}")
    private String bucket;

    @Override
    public void armazenar(PdfResponse response) {
        String numero = response.getNumeroOrcamento();
        String nomeArquivo = response.getNomeArquivo();

        if (nomeArquivo == null || nomeArquivo.isBlank()) {
            log.warn("Resposta de PDF sem nomeArquivo para orçamento {}", numero);
            return;
        }

        arquivosS3.put(numero, nomeArquivo);
        log.info("PDF do orçamento {} registrado no S3 como {}", numero, nomeArquivo);
    }

    @Override
    public byte[] obterPorNumeroOrcamento(String numero) {
        String nomeArquivo = arquivosS3.get(numero);

        if (nomeArquivo == null) {
            log.warn("Nenhum arquivo S3 registrado para orçamento {}", numero);
            return null;
        }

        try {
            GetObjectRequest request = GetObjectRequest.builder()
                    .bucket(bucket)
                    .key(nomeArquivo)
                    .build();

            ResponseBytes<GetObjectResponse> objeto = s3Client.getObjectAsBytes(request);
            return objeto.asByteArray();
        } catch (Exception e) {
            log.error("Falha ao buscar PDF do S3 para orçamento {}: {}", numero, nomeArquivo, e);
            return null;
        }
    }
}
