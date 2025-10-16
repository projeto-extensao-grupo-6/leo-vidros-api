package com.project.extension.service;

import com.project.extension.entity.Cliente;
import com.project.extension.repository.ClienteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class ClienteService {

    private final ClienteRepository repository;

    public Cliente cadastrar(Cliente cliente){
        Cliente novoCliente = repository.save(cliente);
        return novoCliente;
    }

    public List<Cliente> listar() {
        List<Cliente> clientes = repository.findAll();
        return clientes;
    }

    public Cliente atualizar(Cliente clienteAtualizado){
        Integer idCliente = clienteAtualizado.getId();
        List<Cliente> clientes = repository.findAll();

        for (Cliente cliente: clientes) {
            if(cliente.getId().equals(idCliente)){
                cliente.setNome(clienteAtualizado.getNome());
                cliente.setCpf(clienteAtualizado.getCpf());
                cliente.setEmail(cliente.getEmail());
                cliente.setTelefone(cliente.getEmail());
                cliente.setEnderecos(cliente.getEnderecos());
            }
        }
        return clienteAtualizado;
    }

    public void deletar(Integer id){
        repository.deleteById(id);
    }
}
