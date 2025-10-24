package com.project.extension.service;

import com.project.extension.entity.Funcionario;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.exception.naoencontrado.FuncionarioNaoEncontradoException;
import com.project.extension.repository.FuncionarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@AllArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository repository;

    public Funcionario cadastrar(Funcionario funcionario) {
        Funcionario funcionarioSalvo = repository.save(funcionario);
        log.info("Funcionário salvo com sucesso!");
        return funcionarioSalvo;
    }

    public Funcionario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Funcionário com ID " + id + " não encontrado");
            return new FuncionarioNaoEncontradoException();
        });
    }

    public Funcionario buscarPorTelefone(String telefone) {
        return repository.findByTelefone(telefone);
    }

    public List<Funcionario> listar() {
        List<Funcionario> funcionarios = repository.findAll();
        log.info("Total de funcionários encontrados: " + funcionarios.size());
        return funcionarios;
    }

    private void atualizarCampos(Funcionario destino, Funcionario origem) {
        destino.setNome(origem.getNome());
        destino.setTelefone(origem.getTelefone());
        destino.setFuncao(origem.getFuncao());
        destino.setContrato(origem.getContrato());
        destino.setAtivo(origem.getAtivo());
    }

    public Funcionario editar(Funcionario origem, Integer id) {
        Funcionario destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Funcionario funcionarioAtualizado = this.cadastrar(destino);
        log.info("Funcionário atualizado com sucesso!");
        return funcionarioAtualizado;
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info("Funcionário deletado com sucesso");
    }
}
