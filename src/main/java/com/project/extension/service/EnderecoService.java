package com.project.extension.service;

import com.project.extension.entity.Endereco;
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
    private final LogService logService;

    public Endereco cadastrar(Endereco endereco) {
        Endereco enderecoSalvo = repository.save(endereco);

        logService.success(String.format(
                "Novo Endereço ID %d cadastrado. CEP: %s, Rua: %s.",
                enderecoSalvo.getId(),
                enderecoSalvo.getCep(),
                enderecoSalvo.getRua()
        ));

        return enderecoSalvo;
    }

    public Endereco buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Endereço com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.warn("Endereço com ID {} não encontrado", id);
            return new EnderecoNaoEncontradoException();
        });
    }

    public Endereco buscarPorCep(String cep) {
        Endereco endereco = repository.findByCep(cep);
        if (endereco == null) {
            logService.info(String.format("Nenhum endereço encontrado para o CEP %s.", cep));
        }
        return endereco;
    }

    public List<Endereco> listar() {
        List<Endereco> enderecos = repository.findAll();
        logService.info(String.format(
                "Busca por todos os endereços realizada. Total de registros: %d.",
                enderecos.size()
        ));
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
        log.trace("Campos do endereço atualizados em memória.");
    }

    public Endereco editar(Endereco origem, Integer id) {
        Endereco destino = this.buscarPorId(id);

        atualizarCampos(destino, origem);

        Endereco atualizado = repository.save(destino);

        logService.info(String.format(
                "Endereço ID %d atualizado com sucesso. Novo CEP: %s.",
                atualizado.getId(),
                atualizado.getCep()
        ));

        return atualizado;
    }

    public void deletar(Integer id) {
        Endereco endereco = this.buscarPorId(id);

        repository.deleteById(id);

        logService.info(String.format(
                "Endereço ID %d (Rua: %s) deletado com sucesso.",
                id,
                endereco.getRua()
        ));
    }
}