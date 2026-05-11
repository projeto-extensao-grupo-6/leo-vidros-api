package com.project.extension.integration;

import com.project.extension.rabbitmq.queue.PdfResponse;
import com.project.extension.strategy.pdf.S3PdfStorageStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3;

@Testcontainers
class S3PdfStorageStrategyIT {

    private static final String BUCKET = "test-bucket";

    @Container
    static LocalStackContainer localstack = new LocalStackContainer(
            DockerImageName.parse("localstack/localstack:3"))
            .withServices(S3);

    @TempDir
    Path tempDir;

    private S3Client s3Client;
    private S3PdfStorageStrategy strategy;

    @BeforeEach
    void setUp() throws Exception {
        s3Client = S3Client.builder()
                .endpointOverride(localstack.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create("test", "test")))
                .region(Region.of(localstack.getRegion()))
                .build();

        s3Client.createBucket(CreateBucketRequest.builder().bucket(BUCKET).build());

        strategy = new S3PdfStorageStrategy(s3Client);
        org.springframework.test.util.ReflectionTestUtils.setField(strategy, "bucket", BUCKET);
        org.springframework.test.util.ReflectionTestUtils.setField(strategy, "storagePath", tempDir.toString());
    }

    @Test
    void deveArmazenarPdfBaixandoDoS3() {
        byte[] conteudo = "conteudo-pdf-teste".getBytes();
        String nomeArquivo = "orcamento_ORC-001.pdf";

        s3Client.putObject(
                PutObjectRequest.builder().bucket(BUCKET).key(nomeArquivo).build(),
                RequestBody.fromBytes(conteudo));

        PdfResponse response = new PdfResponse("ORC-001", conteudo);
        response.setNomeArquivo(nomeArquivo);
        strategy.armazenar(response);

        byte[] recuperado = strategy.obterPorNumeroOrcamento("ORC-001");

        assertNotNull(recuperado, "PDF deveria estar no cache após armazenar");
        assertArrayEquals(conteudo, recuperado);
    }

    @Test
    void deveRetornarNullParaOrcamentoInexistente() {
        byte[] resultado = strategy.obterPorNumeroOrcamento("ORC-INEXISTENTE");
        assertNull(resultado);
    }

    @Test
    void deveIgnorarNumeroOrcamentoInvalido() {
        PdfResponse response = new PdfResponse();
        response.setNomeArquivo("orcamento.pdf");

        strategy.armazenar(response);

        byte[] resultado = strategy.obterPorNumeroOrcamento("ORC-SEM-CACHE");
        assertNull(resultado);
    }
}
