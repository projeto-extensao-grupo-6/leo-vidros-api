package com.project.extension.service;

import com.project.extension.entity.Etapa;
import com.project.extension.entity.Servico;
import com.project.extension.exception.naoencontrado.ServicoNaoEncontradoException;
import com.project.extension.repository.ServicoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ServicoService {

    private final ServicoRepository repository;
    private final EtapaService etapaService;
    private final LogService logService;

    public Servico cadastrar(Servico servico) {
        if (servico.getCodigo() == null) {
            Servico ultimo = repository.findUltimoServico();

            int proximoNumero = 1;

            if (ultimo != null && ultimo.getCodigo() != null) {
                String codigo = ultimo.getCodigo().replace("#", "");
                proximoNumero = Integer.parseInt(codigo) + 1;
            }

            String novoCodigo = String.format("#%03d", proximoNumero);
            servico.setCodigo(novoCodigo);
        }

        if (servico.getEtapa() == null) {
            Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", "PENDENTE");
            servico.setEtapa(etapa);
        }

        Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", servico.getEtapa().getNome());
        servico.setEtapa(etapa);

        Servico servicoSalvo = repository.save(servico);
        log.info("Novo pedido de serviço com nome: {} registrado com sucesso!", servicoSalvo.getNome());
        return servicoSalvo;
    }

    public Servico buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Serviço com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.warn("Serviço com ID {} não encontrado", id);
            return new ServicoNaoEncontradoException();
        });
    }

    public List<Servico> listarPorEtapa(String nome) {
       if (nome != null) {
           Etapa etapa = etapaService.buscarPorTipoAndEtapa("PEDIDO", nome);
           return repository.findAllByEtapa(etapa);
       }

       return this.listar();
    }

    public List<Servico> listar() {
        List<Servico> servicos = repository.findAll();
        logService.info(String.format("Busca por todos os serviços. Total de registros: %d.", servicos.size()));
        return servicos;
    }

    private void atualizarCampos(Servico destino, Servico origem) {
        destino.setNome(origem.getNome());
        destino.setDescricao(origem.getDescricao());
        destino.setPrecoBase(origem.getPrecoBase());
        destino.setAtivo(origem.getAtivo());
        log.trace("Campos do serviço atualizados em memória.");
    }

    public void atualizarEtapa(Servico destino, Servico origem) {
        destino.setEtapa(etapaService.buscarPorTipoAndEtapa("PEDIDO", origem.getEtapa().getNome()));
    }

    public Servico editar(Servico origem, Integer id) {
        Servico destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        this.atualizarEtapa(destino, origem);
        Servico servicoAtualizado = repository.save(destino);
        log.info("Serviço com nome: {}  atualizado com sucesso!", servicoAtualizado.getNome());
        return servicoAtualizado;
    }

    public void deletar(Integer id) {
        Servico servicoParaDeletar = this.buscarPorId(id);
        repository.deleteById(id);
        String mensagem = String.format("Serviço ID %d (Nome: %s) deletado com sucesso.",
                id, servicoParaDeletar.getNome());
        logService.info(mensagem);
    }
}