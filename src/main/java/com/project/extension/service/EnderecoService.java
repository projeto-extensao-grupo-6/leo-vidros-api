package com.project.extension.service;

import com.project.extension.entity.Endereco;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.exception.naoencontrado.EnderecoNaoEncontradoException;
import com.project.extension.repository.EnderecoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class EnderecoService {

    private final EnderecoRepository repository;

    public Endereco cadastrar(Endereco endereco) {
        Endereco enderecoSalvo = repository.save(endereco);
        log.info("Endereço salvo com sucesso!");
        return enderecoSalvo;
    }

    public Endereco buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Agendamento com ID {} não encontrado", id);
            return new EnderecoNaoEncontradoException();
        });
    }

    public Endereco buscarPorCep(String cep) {
        return repository.findByCep(cep);
    }

    public List<Endereco> listar() {
        List<Endereco> enderecos = repository.findAll();
        log.info("Total de endereçocs encontrados: " + enderecos.size());
        return enderecos;
    }

    private void atualizarCampos(Endereco destino, Endereco origem) {
        destino.setRua(origem.getRua());
        destino.setCep(origem.getCep());
        destino.setBairro(origem.getBairro());
        destino.setCidade(origem.getCidade());
        destino.setComplemento(origem.getComplemento());
        destino.setPais(origem.getPais());
        destino.setUf(origem.getUf());
    }

    public Endereco editar(Endereco origem, Integer id) {
        Endereco destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Endereco enderecoAtualizado = this.cadastrar(destino);
        log.info("Endereço atualizado com sucesso!");
        return enderecoAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Endereço deletado com sucesso");
    }
}
