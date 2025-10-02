package com.project.extension.service;

import com.project.extension.entity.Servico;
import com.project.extension.exception.naoencontrado.ServicoNaoEncontradoException;
import com.project.extension.repository.ServicoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class ServicoService {

    private final ServicoRepository repository;
    private final EnderecoService enderecoService;


    public Servico cadastrar(Servico servico) {
        this.definirFk(servico);
        Servico servicoSalvo = repository.save(servico);
        log.info("Serviço salvo com sucesso!");
        return servicoSalvo;
    }

    private void definirFk(Servico servico) {
        servico.setEndereco(enderecoService.buscarPorId(servico.getEndereco().getId()));
    }

    public Servico buscarPorId(Integer id){
        return repository.findById(id)
                .orElseThrow(ServicoNaoEncontradoException::new);
    }

    public List<Servico> listar() {
        List<Servico> servicos = repository.findAll();
        log.info("Quantidade de serviços encontrados: " + servicos.size());
        return servicos;
    }

    private void atualizarCampos(Servico origem, Servico destino) {
        origem.setData(destino.getData());
        origem.setHorario(destino.getHorario());
        origem.setTipoServico(destino.getTipoServico());
        origem.setTipoVidro(destino.getTipoVidro());
        origem.setDescricao(destino.getDescricao());
        origem.setTipoMaterialAuxiliares(destino.getTipoMaterialAuxiliares());
        origem.setEndereco(destino.getEndereco());
    }

    public Servico editar(Integer id, Servico origem){
        Servico destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Servico atualizado = this.cadastrar(destino);
        log.info("Serviço atualizado com sucesso!");
        return atualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Serviço deletado com sucesso");
    }

//    public List<Servico> listarPorDia(LocalDate data) {
//        List<Servico> servicosDoDia = repository.findAllByData(data);
//        log.info("Quantidade de serviços: {}  agendados para o dia: {}", servicosDoDia.size(),data);
//        return servicosDoDia;
//    }
//
//    public List<Servico> listarPorMes(int mes) {
//        List<Servico> servicosDoMes = repository.findAllByMes(mes);
//        log.info("Quantidade de serviços no mês {}: {}", mes, servicosDoMes.size());
//        return servicosDoMes;
//    }

    // listar por cliente

    // listar por tipo de serviço

    // listar por funcionario alocado

    // listar por serviço concluido
}
