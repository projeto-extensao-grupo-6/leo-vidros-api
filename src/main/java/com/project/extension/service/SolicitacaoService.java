package com.project.extension.service;

import com.project.extension.dto.usuario.UsuarioMapper;
import com.project.extension.dto.usuario.UsuarioRequestDto;
import com.project.extension.entity.Solicitacao;
import com.project.extension.entity.Status;
import com.project.extension.entity.Usuario;
import com.project.extension.repository.SolicitacaoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final UsuarioMapper usuarioMapper;

    public Solicitacao cadastrar(Solicitacao solicitacao) {
        solicitacao.setStatus(Status.PENDENTE);
        return repository.save(solicitacao);
    }

    public List<Solicitacao> listarPendentes() {
        return repository.findByStatus(Status.PENDENTE);
    }

    public void aceitarSolicitacao(Integer id) {
        repository.findById(id).ifPresentOrElse(solicitacao -> {
            solicitacao.setStatus(Status.APROVADO);
            repository.save(solicitacao);

            try {
                criarUsuarioEEnviarEmail(solicitacao);
            } catch (Exception e) {
                log.error("Erro ao criar usuário ou enviar email: {}", e.getMessage());
            }
        }, () -> log.warn("Solicitação não encontrada: id={}", id));
    }

    public void recusarSolicitacao(Integer id) {
        repository.findById(id).ifPresent(solicitacao -> {
            solicitacao.setStatus(Status.REJEITADO);
            repository.save(solicitacao);

            enviarEmailRecusa(solicitacao.getNome(), solicitacao.getEmail());
        });
    }

    private void criarUsuarioEEnviarEmail(Solicitacao solicitacao) {
        String senhaTemporaria = gerarSenhaTemporaria();
        log.debug("Senha temporária gerada: {}", senhaTemporaria);

        String senhaCriptografada = usuarioService.encodePassword(senhaTemporaria);

        Usuario usuario = new Usuario(
                solicitacao.getNome(),
                solicitacao.getEmail(),
                solicitacao.getCpf(),
                senhaCriptografada,
                solicitacao.getTelefone(),
                true
        );

        usuarioService.salvar(usuario);
        log.info("Usuário criado: email={}", usuario.getEmail());


        enviarEmailAceite(usuario.getNome(), usuario.getEmail(), senhaTemporaria);
    }

    private String gerarSenhaTemporaria() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private void enviarEmailAceite(String nomeUsuario, String email, String senha) {
        String conteudoHtml = emailService.gerarEmailAceito(nomeUsuario, email, senha);
        emailService.enviarEmail(email, "Solicitação Aceita", conteudoHtml);
        log.info("Email enviado para {}", email);
    }

    private void enviarEmailRecusa(String nomeUsuario, String email) {
        String conteudoHtml = emailService.gerarEmailRecusado(nomeUsuario);
        emailService.enviarEmail(email, "Solicitação Recusada", conteudoHtml);
        log.info("Email de recusa enviado para {}", email);
    }
}