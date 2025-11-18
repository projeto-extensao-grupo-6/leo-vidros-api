package com.project.extension.service;

import com.project.extension.entity.Cliente;
import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
import com.project.extension.entity.Status;
import com.project.extension.exception.naoencontrado.PedidoNaoEncontradoException;
import com.project.extension.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {
    private final PedidoRepository repository;
    private final StatusService statusService;
    private final EtapaService etapaService;
    private final ClienteService clienteService;
    private final LogService logService;

    public Pedido cadastrar(Pedido pedido) {
        Status statusSalvo = statusService.buscarPorTipoAndStatus(
                pedido.getStatus().getTipo(),
                pedido.getStatus().getNome()
        );

        if (statusSalvo == null) {
            statusSalvo = statusService.cadastrar(pedido.getStatus());
            logService.info(String.format("Status criado automaticamente para Pedido: %s - %s.",
                    statusSalvo.getTipo(), statusSalvo.getNome()));
        }

        Etapa etapaSalvo = etapaService.buscarPorTipoAndEtapa(
                pedido.getEtapa().getTipo(),
                pedido.getEtapa().getNome()
        );

        if (etapaSalvo == null) {
            etapaSalvo = etapaService.cadastrar(pedido.getEtapa());
            logService.info(String.format("Etapa criada automaticamente para Pedido: %s - %s.",
                    etapaSalvo.getTipo(), etapaSalvo.getNome()));
        }

        Cliente clienteAssociado = clienteService.buscarPorId(
                pedido.getCliente().getId()
        );

        if (clienteAssociado == null){
            clienteAssociado = clienteService.cadastrar(pedido.getCliente());
            log.info("ID Client: {} - Cliente associado: {}", clienteAssociado.getId(), clienteAssociado.getNome());
        }

        pedido.setEtapa(etapaSalvo);
        pedido.setStatus(statusSalvo);

        Pedido pedidoSalvo = repository.save(pedido);
        String mensagem = String.format("Novo Pedido ID %d cadastrado com sucesso. Status: %s, Etapa: %s.",
                pedidoSalvo.getId(),
                statusSalvo.getNome(),
                etapaSalvo.getNome());
        logService.success(mensagem);

        return pedidoSalvo;
    }

    public Pedido buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Pedido com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.error("Pedido com ID {} não encontrado", id);
            return new PedidoNaoEncontradoException();
        });
    }

    public List<Pedido> listar() {
        List<Pedido> pedidos = repository.findAll();
        logService.info(String.format("Busca por todos os pedidos realizada. Total de registros: %d.", pedidos.size()));
        return pedidos;
    }

    private void atualizarCampos(Pedido destino, Pedido origem) {
       destino.setValorTotal(origem.getValorTotal());
       destino.setAtivo(origem.getAtivo());
       destino.setObservacao(origem.getObservacao());

        if (origem.getStatus() != null) {
            Status statusAtualizado = statusService.buscarPorTipoAndStatus(origem.getStatus().getTipo(),
                    origem.getStatus().getNome());
            destino.setStatus(statusAtualizado);
        }

        if (origem.getEtapa() != null) {
            Etapa etapaAtualizada = etapaService.buscarPorTipoAndEtapa(
                    origem.getEtapa().getTipo(),
                    origem.getEtapa().getNome()
            );
            destino.setEtapa(etapaAtualizada);
        }
    }

    public Pedido editar(Pedido origem, Integer id) {
        Pedido destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Pedido pedidoAtualizado = this.cadastrar(destino);
        String mensagem = String.format("Pedido ID %d atualizado com sucesso. Valor Total: %.2f.",
                pedidoAtualizado.getId(),
                pedidoAtualizado.getValorTotal());
        logService.info(mensagem);
        return pedidoAtualizado;
    }

    public void deletar(Integer id) {
        Pedido pedidoParaDeletar = this.buscarPorId(id);
        String mensagem = String.format("Pedido ID %d (Status: %s) deletado com sucesso.",
                id,
                pedidoParaDeletar.getStatus().getNome());
        logService.info(mensagem);
    }
}
