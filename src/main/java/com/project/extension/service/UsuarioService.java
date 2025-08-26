package com.project.extension.service;

import com.project.extension.entity.Role;
import com.project.extension.entity.Usuario;
import com.project.extension.exception.naoencontrado.UsuarioNaoEncontradoException;
import com.project.extension.repository.UsuarioRepository;
import com.project.extension.util.LoggerUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final RoleService roleService;

    public Usuario salvar(Usuario usuario, String nomeRole) {
        Role roleExistente = roleService.buscarPorNome(nomeRole);
        usuario.setRole(roleExistente);
        Usuario salvo = repository.save(usuario);
        LoggerUtils.info("Usuário salvo com ID: " + salvo.getId());

        return salvo;
    }

    public Usuario buscarPorId(Integer id) {
        return repository.findById(id).orElseThrow(() -> {
            LoggerUtils.error("Usuário com ID " + id + " não encontrado");
            return new UsuarioNaoEncontradoException();
        });
    }

    public List<Usuario> buscarTodos() {
        List<Usuario> lista = repository.findAll();
        LoggerUtils.info("Total de usuários encontrados: " + lista.size());
        return lista;
    }

    private void atualizarCampos(Usuario destino, Usuario origem) {
        destino.setNome(origem.getNome());
        destino.setCpf(origem.getCpf());
        destino.setEmail(origem.getEmail());
        destino.setSenha(origem.getSenha());
        destino.setRole(origem.getRole());
    }

    public void deletar(Integer id) {
        repository.deleteById(id);
        LoggerUtils.info( "Usuário deletado com sucesso");
    }

    public String buscarPorEmail(@NotBlank String email) {
        return repository.findByEmail(email).orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Usuario editar(Integer id, Usuario usuarioAtualizado, String nomeRole) {
        Usuario usuarioExistente = buscarPorId(id);

        atualizarCampos(usuarioExistente, usuarioAtualizado);

        if (nomeRole != null && !nomeRole.isEmpty()) {
            Role roleExistente = roleService.buscarPorNome(nomeRole);
            usuarioExistente.setRole(roleExistente);
        } else if (usuarioExistente.getRole() == null) {
            Role rolePadrao = roleService.buscarPorNome("COMUM");
            usuarioExistente.setRole(rolePadrao);
        }

        Usuario atualizado = repository.save(usuarioExistente);
        LoggerUtils.info("Usuário atualizado com sucesso");
        return atualizado;
    }
}
