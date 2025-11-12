package com.project.extension.service;

import com.project.extension.entity.Usuario;
import com.project.extension.exception.naoencontrado.UsuarioNaoEncontradoException;
import com.project.extension.repository.UsuarioRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class UsuarioService {
    private final UsuarioRepository repository;
    private final LogService logService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(Usuario usuario) {
        try {
            Usuario salvo = repository.save(usuario);
            String acao = (usuario.getId() == null) ? "criado" : "salvo";
            String mensagem = String.format("Usuário ID %d %s com sucesso. E-mail: %s.",
                    salvo.getId(), acao, salvo.getEmail());
            logService.success(mensagem);
            return salvo;
        } catch (Exception e) {
            logService.fatal(String.format("Erro FATAL ao salvar usuário: %s. E-mail: %s.",
                    e.getMessage(), usuario.getEmail()), e);
            log.error("Erro ao salvar usuário: " + e.getMessage());
            throw new RuntimeException("Não foi possível salvar o usuário");
        }
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Usuário com ID %d não encontrado.", id);
            logService.error(mensagem);
            log.error("Usuário com ID " + id + " não encontrado");
            return new UsuarioNaoEncontradoException();
        });
    }

    public List<Usuario> buscarTodos() {
        List<Usuario> lista = repository.findAll();
        logService.info(String.format("Busca por todos os usuários realizada. Total de usuários: %d.", lista.size()));
        return lista;
    }

    private void atualizarCampos(Usuario destino, Usuario origem) {
        destino.setNome(origem.getNome());
        destino.setCpf(origem.getCpf());
        destino.setEmail(origem.getEmail());
        destino.setTelefone(origem.getTelefone());
        destino.setSenha(origem.getSenha());
        if (origem.getSenha() != null && !origem.getSenha().isEmpty()) {
            destino.setSenha(origem.getSenha());
            logService.warning(String.format("Usuário ID %d: Senha alterada (apenas registro de ação).", destino.getId()));
        }
        log.trace("Campos do usuário atualizados em memória.");
    }

    public void deletar(Integer id) {
        Usuario usuarioParaDeletar = this.buscarPorId(id);
        repository.deleteById(id);

        String mensagem = String.format("Usuário ID %d (E-mail: %s) deletado com sucesso.",
                id, usuarioParaDeletar.getEmail());
        logService.info(mensagem);
    }

    public Usuario buscarPorEmail(@NotBlank String email) {
        return repository.findByEmail(email).orElseThrow(() -> {
            String mensagem = String.format("Falha na busca: Usuário com e-mail '%s' não encontrado.", email);
            logService.warning(mensagem);
            log.error(mensagem);
            return new UsuarioNaoEncontradoException();
        });
    }

    public Usuario editar(Integer id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);

        atualizarCampos(usuarioExistente, usuarioAtualizado);
        Usuario atualizado = repository.save(usuarioExistente);
        String mensagem = String.format("Usuário ID %d editado com sucesso. E-mail: %s.",
                atualizado.getId(), atualizado.getEmail());
        logService.info(mensagem);
        return atualizado;
    }

    public String encodePassword(String senha) {
        return passwordEncoder.encode(senha);
    }
}
