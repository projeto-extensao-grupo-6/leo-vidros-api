package com.project.extension.service;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.ClienteNaoEncontradoException;
import com.project.extension.exception.naoencontrado.UsuarioNaoEncontradoException;
import com.project.extension.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;
    private final EnderecoService enderecoService;
    private final StatusService statusService;

    public Cliente cadastrar(Cliente cliente){
        Cliente novoCliente = repository.save(cliente);
        log.info("Cliente salvo com sucesso!");
        return novoCliente;
    }

    public Cliente buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Cliente com ID " + id + " não encontrado");
            return new ClienteNaoEncontradoException();
        });
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = repository.findAll();
        log.info("Total de clientes encontrados: " + clientes.size());
        return clientes;
    }

    public Cliente atualizar(Cliente origem, Integer id){
       Cliente destino = this.buscarPorId(id);

       atualizarDadosBasicos(destino, origem);
       atualizarEnderecos(destino, origem);
       atualizarStatus(destino, origem);

       Cliente clienteAtualizado = repository.save(destino);
       log.info("Cleinte atualizado com sucesso! ID: {}", clienteAtualizado.getId());
       return clienteAtualizado;
    }

    public void deletar(Integer id){
        repository.deleteById(id);
    }

    private void atualizarDadosBasicos(Cliente destino, Cliente origem) {
        destino.setNome(origem.getNome());
        destino.setCpf(origem.getCpf());
        destino.setEmail(origem.getEmail());
        destino.setTelefone(origem.getTelefone());
    }

    private void atualizarEnderecos(Cliente destino, Cliente origem) {
        if (origem.getEnderecos() != null) {
            List<Endereco> enderecosAtualizados = origem.getEnderecos().stream()
                    .map(endereco -> {
                        if (endereco.getId() != null) {
                            return enderecoService.editar(endereco, endereco.getId());
                        } else {
                            return enderecoService.cadastrar(endereco);
                        }
                    })
                    .collect(Collectors.toList());

            destino.setEnderecos(enderecosAtualizados);
        }
    }

    private void atualizarStatus(Cliente destino, Cliente origem) {
        if (origem.getStatus() != null) {
            Status statusAtualizado = statusService.buscarOuCriarPorTipoENome(
                    origem.getStatus().getTipo(),
                    origem.getStatus().getNome()
            );
            destino.setStatus(statusAtualizado);
        }
    }
}
