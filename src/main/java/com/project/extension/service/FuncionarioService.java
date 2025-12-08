package com.project.extension.service;

import com.project.extension.entity.Funcionario;
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
    private final LogService logService;

    public Funcionario cadastrar(Funcionario funcionario) {
        Funcionario funcionarioSalvo = repository.save(funcionario);
        String mensagem = String.format("Novo Funcionário ID %d cadastrado com sucesso. Nome: %s, Função: %s.",
                funcionarioSalvo.getId(), funcionarioSalvo.getNome(), funcionarioSalvo.getFuncao());
        logService.success(mensagem);
        return funcionarioSalvo;
    }

    public Funcionario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Funcionário com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.warn("Funcionário com ID {} não encontrado", id);
            return new FuncionarioNaoEncontradoException();
        });
    }

    public Funcionario buscarPorTelefone(String telefone) {
        return repository.findByTelefone(telefone);
    }

    public List<Funcionario> listar() {
        List<Funcionario> funcionarios = repository.findAll();
        logService.info(String.format("Busca por todos os funcionários realizada. Total de registros: %d.", funcionarios.size()));
        return funcionarios;
    }

    private void atualizarCampos(Funcionario destino, Funcionario origem) {
        destino.setNome(origem.getNome());
        destino.setTelefone(origem.getTelefone());
        destino.setFuncao(origem.getFuncao());
        destino.setContrato(origem.getContrato());
        destino.setEscala(origem.getEscala()); // <- ADICIONADO
        destino.setAtivo(origem.getAtivo());   // <- NECESSÁRIO POR CAUSA DO STATUS
        destino.setAtivo(origem.getAtivo());
        log.trace("Campos do funcionário atualizados em memória.");
    }

    public Funcionario editar(Funcionario origem, Integer id) {
        Funcionario destino = this.buscarPorId(id);
        this.atualizarCampos(destino, origem);
        Funcionario funcionarioAtualizado = repository.save(destino);
        String mensagem = String.format("Funcionário ID %d atualizado com sucesso. Nome: %s, Função: %s.",
                funcionarioAtualizado.getId(), funcionarioAtualizado.getNome(), funcionarioAtualizado.getFuncao());
        logService.info(mensagem);
        return funcionarioAtualizado;
    }

    public void deletar(Integer id) {
        Funcionario funcionarioParaDeletar = this.buscarPorId(id);
        repository.deleteById(id);
        String mensagem = String.format("Funcionário ID %d (Nome: %s) deletado com sucesso.",
                id, funcionarioParaDeletar.getNome());
        logService.info(mensagem);
    }
}
