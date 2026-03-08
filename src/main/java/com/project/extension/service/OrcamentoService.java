package com.project.extension.service;

import com.project.extension.config.RabbitMQConfig;
import com.project.extension.dto.orcamento.*;
import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.OrcamentoNaoEncontradoException;
import com.project.extension.repository.OrcamentoRepository;
import com.project.extension.repository.ProdutoRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class OrcamentoService {

    private final OrcamentoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final PedidoService pedidoService;
    private final ClienteService clienteService;
    private final StatusService statusService;
    private final LogService logService;
    private final RabbitTemplate rabbitTemplate;
    private final OrcamentoSseService sseService;

    @Transactional
    public Orcamento criar(OrcamentoRequestDto request) {
        // 1. Buscar entidades relacionadas
        Pedido pedido = pedidoService.buscarPorId(request.pedidoId());

        Integer clienteId = request.clienteId() != null
                ? request.clienteId()
                : (pedido.getCliente() != null ? pedido.getCliente().getId() : null);

        Cliente cliente = clienteId != null
                ? clienteService.buscarPorId(clienteId)
                : pedido.getCliente();

        String statusNome = request.statusNome() != null ? request.statusNome() : "RASCUNHO";
        Status status = statusService.buscarOuCriarPorTipoENome("ORCAMENTO", statusNome);

        // 2. Construir entidade Orcamento
        Orcamento orcamento = new Orcamento();
        orcamento.setPedido(pedido);
        orcamento.setCliente(cliente);
        orcamento.setStatus(status);
        orcamento.setNumeroOrcamento(request.numeroOrcamento());
        orcamento.setDataOrcamento(LocalDate.parse(request.dataOrcamento()));
        orcamento.setObservacoes(request.observacoes());
        orcamento.setPrazoInstalacao(request.prazoInstalacao());
        orcamento.setGarantia(request.garantia());
        orcamento.setFormaPagamento(request.formaPagamento());
        orcamento.setValorSubtotal(request.valorSubtotal() != null ? request.valorSubtotal() : BigDecimal.ZERO);
        orcamento.setValorDesconto(request.valorDesconto() != null ? request.valorDesconto() : BigDecimal.ZERO);
        orcamento.setValorTotal(request.valorTotal() != null ? request.valorTotal() : BigDecimal.ZERO);

        // 3. Adicionar itens
        if (request.itens() != null) {
            for (OrcamentoItemRequestDto itemDto : request.itens()) {
                OrcamentoItem item = new OrcamentoItem();
                item.setDescricao(itemDto.descricao());
                item.setQuantidade(itemDto.quantidade());
                item.setPrecoUnitario(itemDto.precoUnitario());
                item.setDesconto(itemDto.desconto() != null ? itemDto.desconto() : BigDecimal.ZERO);
                item.setObservacao(itemDto.observacao());
                item.setOrdem(itemDto.ordem() != null ? itemDto.ordem() : 0);

                if (itemDto.produtoId() != null) {
                    produtoRepository.findById(itemDto.produtoId())
                            .ifPresent(item::setProduto);
                }

                orcamento.adicionarItem(item);
            }
        }

        // 4. Salvar no banco
        Orcamento salvo = repository.save(orcamento);

        logService.success(String.format(
                "Orçamento ID %d criado com sucesso. Número: %s, Pedido: %d, Total: %s.",
                salvo.getId(),
                salvo.getNumeroOrcamento(),
                salvo.getPedido().getId(),
                salvo.getValorTotal()
        ));

        return salvo;
    }

    /**
     * Cria o orçamento, salva no banco, e publica a mensagem no RabbitMQ
     * para geração assíncrona do PDF. Envia eventos SSE para o frontend.
     */
    @Transactional
    public Orcamento criarEGerarPdf(OrcamentoRequestDto request) {
        // Emite evento SSE: Gerando orçamento
        sseService.enviarEvento(request.pedidoId(), "GERANDO_ORCAMENTO");

        // 1. Salvar no banco
        Orcamento salvo = criar(request);

        // Emite evento SSE: Gerando PDF
        sseService.enviarEvento(salvo.getId(), "GERANDO_PDF");

        // 2. Publicar na fila RabbitMQ
        OrcamentoMensagemDto mensagem = montarMensagem(salvo);
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE_NAME,
                    RabbitMQConfig.ROUTING_KEY,
                    mensagem
            );
            log.info("Mensagem publicada na fila RabbitMQ para o orçamento ID {}.", salvo.getId());
            logService.info(String.format(
                    "Mensagem de geração de PDF publicada na fila para o Orçamento ID %d.",
                    salvo.getId()
            ));
        } catch (Exception e) {
            log.error("Erro ao publicar mensagem no RabbitMQ para orçamento ID {}.", salvo.getId(), e);
            logService.error(String.format(
                    "Falha ao publicar mensagem no RabbitMQ para Orçamento ID %d: %s",
                    salvo.getId(), e.getMessage()
            ));
            sseService.enviarEvento(salvo.getId(), "ERRO");
        }

        return salvo;
    }

    public Orcamento buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String msg = String.format("Orçamento ID %d não encontrado.", id);
            logService.error(msg);
            return new OrcamentoNaoEncontradoException();
        });
    }

    public List<Orcamento> listar() {
        List<Orcamento> orcamentos = repository.findByAtivoTrueOrderByCreatedAtDesc();
        logService.info(String.format("Listagem de orçamentos: %d registros.", orcamentos.size()));
        return orcamentos;
    }

    public List<Orcamento> listarPorPedido(Integer pedidoId) {
        return repository.findByPedidoIdAndAtivoTrue(pedidoId);
    }

    /**
     * Atualiza o status do orçamento (chamado pelo microserviço após gerar o PDF).
     */
    @Transactional
    public Orcamento atualizarStatus(Integer id, String statusNome, String pdfPath) {
        Orcamento orcamento = buscarPorId(id);
        Status status = statusService.buscarOuCriarPorTipoENome("ORCAMENTO", statusNome);
        orcamento.setStatus(status);

        if (pdfPath != null && !pdfPath.isBlank()) {
            orcamento.setPdfPath(pdfPath);
        }

        Orcamento atualizado = repository.save(orcamento);

        logService.info(String.format(
                "Status do Orçamento ID %d atualizado para '%s'.",
                id, statusNome
        ));

        // Emite evento SSE
        String eventoSse = "ERRO".equalsIgnoreCase(statusNome) ? "ERRO" : "FINALIZADO";
        sseService.enviarEvento(id, eventoSse);

        return atualizado;
    }

    private OrcamentoMensagemDto montarMensagem(Orcamento orcamento) {
        OrcamentoMensagemDto.ClienteMsg clienteMsg = new OrcamentoMensagemDto.ClienteMsg(
                orcamento.getCliente() != null ? orcamento.getCliente().getNome() : "N/A",
                orcamento.getCliente() != null ? orcamento.getCliente().getEmail() : "",
                orcamento.getCliente() != null ? orcamento.getCliente().getTelefone() : ""
        );

        List<OrcamentoMensagemDto.ItemMsg> itensMsg = orcamento.getItens().stream()
                .map(item -> new OrcamentoMensagemDto.ItemMsg(
                        item.getDescricao(),
                        item.getQuantidade() != null ? item.getQuantidade().doubleValue() : 0.0,
                        item.getPrecoUnitario() != null ? item.getPrecoUnitario().doubleValue() : 0.0,
                        item.getDesconto() != null ? item.getDesconto().doubleValue() : 0.0,
                        item.getSubtotal() != null ? item.getSubtotal().doubleValue() : 0.0,
                        item.getObservacao()
                ))
                .toList();

        return new OrcamentoMensagemDto(
                orcamento.getId().longValue(),
                orcamento.getNumeroOrcamento(),
                orcamento.getDataOrcamento() != null ? orcamento.getDataOrcamento().toString() : "",
                clienteMsg,
                itensMsg,
                orcamento.getValorSubtotal() != null ? orcamento.getValorSubtotal().doubleValue() : 0.0,
                orcamento.getValorDesconto() != null ? orcamento.getValorDesconto().doubleValue() : 0.0,
                orcamento.getValorTotal() != null ? orcamento.getValorTotal().doubleValue() : 0.0,
                orcamento.getPrazoInstalacao(),
                orcamento.getGarantia(),
                orcamento.getFormaPagamento(),
                orcamento.getObservacoes()
        );
    }
}
