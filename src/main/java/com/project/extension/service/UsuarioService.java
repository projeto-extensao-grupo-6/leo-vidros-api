package com.project.extension.service;

import com.project.extension.dto.usuario.UsuarioMapper;
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
    private final UsuarioMapper usuarioMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Usuario salvar(Usuario usuario) {
            Usuario salvo = repository.save(usuario);

            if (usuario.getEndereco() != null) {
                Endereco endereco = enderecoService.cadastrar(usuario.getEndereco());
                usuario.setEndereco(endereco);
            }

            String acao = (usuario.getId() == null) ? "criado" : "salvo";
            String mensagem = String.format("Usuário ID %d %s com sucesso. E-mail: %s.",
                    salvo.getId(), acao, salvo.getEmail());
            logService.success(mensagem);
            return salvo;
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

    public void deletar(Integer id) {
        Usuario usuarioParaDeletar = this.buscarPorId(id);
        enderecoService.deletar(usuarioParaDeletar.getEndereco().getId());
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

    private Endereco atualizarEndereco(Endereco antigo, Endereco novo) {
        if (antigo == null && novo != null) {
            return enderecoService.cadastrar(novo);
        }

        if (novo == null) {
            return antigo;
        }

        enderecoService.editar(novo, antigo.getId());
        return enderecoService.buscarPorId(antigo.getId());
    }

    public Usuario editar(Usuario origem, Integer id) {

        Usuario destino = this.buscarPorId(id);

        this.atualizarCampos(destino, origem);
        destino.setEndereco(this.atualizarEndereco(destino.getEndereco(), origem.getEndereco()));

        Usuario atualizado = repository.save(destino);

        logService.info(String.format(
                "Usuário ID %d atualizado com sucesso. Nome: %s.",
                atualizado.getId(),
                atualizado.getNome()
        ));

        return atualizado;
    }

    public String encodePassword(String senha) {
        return passwordEncoder.encode(senha);
    }

    public void definirSenhaInicial(Integer idUsuario, String novaSenha) {
        Usuario usuario = buscarPorId(idUsuario);

        // 1. Criptografa a nova senha
        String senhaCriptografada = passwordEncoder.encode(novaSenha);

        // 2. Atualiza a entidade: seta a senha e firstLogin = false
        // Utilizamos o mapper para aplicar a lógica de atualização (definida anteriormente)
        usuario = usuarioMapper.updateSenha(usuario, senhaCriptografada);

        // 3. Persiste a entidade atualizada
        repository.save(usuario);

        String mensagem = String.format("Senha inicial definida com sucesso para o Usuário ID %d. 'First Login' marcado como FALSE.", idUsuario);
        logService.success(mensagem);
    }
}
