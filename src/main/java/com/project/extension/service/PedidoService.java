package com.project.extension.service;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Pedido;
import com.project.extension.entity.Status;
import com.project.extension.exception.naoencontrado.PedidoNaoEncontradoException;
import com.project.extension.repository.EtapaRepository;
import com.project.extension.repository.PedidoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class PedidoService {

    private final PedidoRepository repository;
    private final StatusService statusService;
    private final EtapaService etapaService;

    public Pedido cadastrar(Pedido pedido) {

        Status statusSalvo = statusService.buscarPorTipoAndStatus(
                pedido.getStatus().getTipo(),
                pedido.getStatus().getNome()
        );

        if (statusSalvo == null) {
            statusSalvo = statusService.cadastrar(pedido.getStatus());
            log.info("Status criado: {} - {}", statusSalvo.getTipo(), statusSalvo.getNome());
        }

        Etapa etapaSalvo = etapaService.buscarPorTipoAndEtapa(
                pedido.getEtapa().getTipo(),
                pedido.getEtapa().getNome()
        );

        if (etapaSalvo == null) {
            etapaSalvo = etapaService.cadastrar(pedido.getEtapa());
            log.info("Etapa criado: {} - {}", etapaSalvo.getTipo(), etapaSalvo.getNome());
        }

        pedido.setEtapa(etapaSalvo);
        pedido.setStatus(statusSalvo);

        Pedido pedidoSalvo = repository.save(pedido);
        log.info("Pedido salvo com sucesso!");

        return pedidoSalvo;
    }


    public Pedido buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Pedido com ID " + id + " não encontrado");
            return new PedidoNaoEncontradoException();
        });
    }

    public List<Pedido> listar() {
        List<Pedido> pedidos = repository.findAll();
        log.info("Total de pedidos encontrados: " + pedidos.size());
        return pedidos;
    }

    public List<Pedido> listarPedidosPorTipoENomeDaEtapa(String nome) {
        Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", nome);
        List<Pedido> pedidos = repository.findAllByEtapa(etapa);
        log.info("Total de pedidos encontrados: " + pedidos.size() + " para etapa: " + etapa.getNome());
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
        log.info("Pedido atualizado com sucesso!");
        return pedidoAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Pedido deletado com sucesso");
    }
}
