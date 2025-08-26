package com.project.extension.service;

import com.project.extension.entity.Role;
import com.project.extension.entity.Solicitacao;
import com.project.extension.entity.Status;
import com.project.extension.entity.Usuario;
import com.project.extension.exception.naoencontrado.RoleNaoEncontradoException;
import com.project.extension.repository.RoleRepository;
import com.project.extension.repository.SolicitacaoRepository;
import com.project.extension.repository.UsuarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final RoleService roleService;
    private final EmailService emailService;

    public Solicitacao cadastrar(Solicitacao solicitacao) {
        solicitacao.setStatus(Status.PENDENTE);
        return repository.save(solicitacao);
    }

    public List<Solicitacao> listarPendentes() {
        return repository.findByStatus(Status.PENDENTE);
    }

    public void aceitarSolicitacao(Integer id, String cargoAlterado) {
        Optional<Solicitacao> opt = repository.findById(id);
        if (opt.isPresent()) {
            Solicitacao solicitacao = opt.get();

            solicitacao.setStatus(Status.APROVADO);
            if (cargoAlterado != null && !cargoAlterado.isEmpty()) {
                solicitacao.setCargo(cargoAlterado);
            }
            repository.save(solicitacao);

            String nomeCargo = cargoAlterado != null ? cargoAlterado : solicitacao.getCargo();

            Role role = roleService.buscarPorNome(nomeCargo);


            String senhaTemporaria = UUID.randomUUID().toString().substring(0, 8);
            Usuario usuario = new Usuario();
            usuario.setNome(solicitacao.getNome());
            usuario.setEmail(solicitacao.getEmail());
            usuario.setCpf(solicitacao.getCpf());
            usuario.setRole(role);
            usuario.setSenha(senhaTemporaria);
            // usuario.setSenha(passwordEncoder.encode(senhaTemporaria));
            usuario.setFirstLogin(true);

            usuarioRepository.save(usuario);


            String mensagem = "Sua solicitação foi aceita. Suas credenciais:\nEmail: "
                    + usuario.getEmail() + "\nSenha temporária: " + senhaTemporaria;
            if (cargoAlterado != null && !cargoAlterado.isEmpty()) {
                mensagem += "\nO cargo foi alterado para: " + cargoAlterado;
            }
            emailService.enviarEmail(usuario.getEmail(), "Solicitação Aceita", mensagem);
        }
    }

    public void recusarSolicitacao(Integer id) {
        Optional<Solicitacao> opt = repository.findById(id);
        if (opt.isPresent()) {
            Solicitacao solicitacao = opt.get();
            solicitacao.setStatus(Status.REJEITADO);
            repository.save(solicitacao);

            String mensagem = "Sua solicitação para acesso à aplicação foi recusada.";
            emailService.enviarEmail(solicitacao.getEmail(), "Solicitação Recusada", mensagem);
        }
    }
}
