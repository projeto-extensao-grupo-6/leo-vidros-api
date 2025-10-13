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

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(Usuario usuario) {
        try {
            Usuario salvo = repository.save(usuario);
            log.info("Usuário salvo com ID: " + salvo.getId());
            return salvo;
        } catch (Exception e) {
            log.error("Erro ao salvar usuário: " + e.getMessage());
            throw new RuntimeException("Não foi possível salvar o usuário");
        }
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            log.error("Usuário com ID " + id + " não encontrado");
            return new UsuarioNaoEncontradoException();
        });
    }

    public List<Usuario> buscarTodos() {
        List<Usuario> lista = repository.findAll();
        log.info("Total de usuários encontrados: " + lista.size());
        return lista;
    }

    private void atualizarCampos(Usuario destino, Usuario origem) {
        destino.setNome(origem.getNome());
        destino.setCpf(origem.getCpf());
        destino.setEmail(origem.getEmail());
        destino.setTelefone(origem.getTelefone());
        destino.setSenha(origem.getSenha());
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        log.info( "Usuário deletado com sucesso");
    }

    public Usuario buscarPorEmail(@NotBlank String email) {
        return repository.findByEmail(email).orElseThrow(() -> {
            log.error("Usuário com e-mail " + email + " não encontrado");
            return new UsuarioNaoEncontradoException();
        });
    }

    public Usuario editar(Integer id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);

        atualizarCampos(usuarioExistente, usuarioAtualizado);
        Usuario atualizado = repository.save(usuarioExistente);
        log.info("Usuário atualizado com sucesso");
        return atualizado;
    }

    public String encodePassword(String senha) {
        return passwordEncoder.encode(senha);
    }
}
