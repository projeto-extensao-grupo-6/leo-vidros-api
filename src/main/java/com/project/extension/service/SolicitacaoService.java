package com.project.extension.service;

import com.project.extension.dto.usuario.UsuarioMapper;
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
    private final StatusService statusService;
    private final LogService logService;

    public Solicitacao cadastrar(Solicitacao solicitacao) {
        Status status = statusService.buscarPorTipoAndStatus("SOLICITACAO", "PENDENTE");
        solicitacao.setStatus(status);
        Solicitacao salvo = repository.save(solicitacao);

        String mensagem = String.format("Nova Solicitacao ID %d criada. Nome: %s, E-mail: %s. Status: PENDENTE.",
                salvo.getId(), salvo.getNome(), salvo.getEmail());
        logService.success(mensagem);
        return salvo;
    }

    public List<Solicitacao> listarPorNome(String nome) {
        List<Solicitacao> listaSolicitacoesPorNomes = nome != null && !nome.isBlank() ? repository.findAllByNomeIgnoreCase(nome) : repository.findAll();
        logService.info(String.format("Busca por solicitação pelo nome '%s' realizada. Total: %d.", nome, listaSolicitacoesPorNomes.size()));
        return listaSolicitacoesPorNomes;
    }

    public List<Solicitacao> listar(String status) {
        return status != null && !status.isBlank() ? repository.findAllByStatusNomeIgnoreCase(status) : repository.findAll();
    }

    public void aceitarSolicitacao(Integer id) {
        repository.findById(id).ifPresentOrElse(solicitacao -> {
            Status aprovado = statusService.buscarPorTipoAndStatus("SOLICITACAO", "ACEITO");
            solicitacao.setStatus(aprovado);
            repository.save(solicitacao);

            logService.info(String.format("Solicitacao ID %d aceita. Status alterado para ACEITO.", id));

            try {
                criarUsuarioEEnviarEmail(solicitacao);
            } catch (Exception e) {
                logService.fatal(String.format("Erro FATAL ao criar usuário e enviar e-mail para Solicitacao ID %d.", id), e);
                log.error("Erro ao criar usuário ou enviar email: {}", e.getMessage());
            }
        }, () -> {
            logService.error(String.format("Tentativa de aceitar Solicitacao ID %d falhou: não encontrada.", id));
            log.warn("Solicitação não encontrada: id={}", id);
        });
    }

    public void recusarSolicitacao(Integer id) {
        repository.findById(id).ifPresentOrElse(solicitacao -> {
            Status recusado = statusService.buscarPorTipoAndStatus("SOLICITACAO", "RECUSADO");
            solicitacao.setStatus(recusado);
            repository.save(solicitacao);

            logService.warning(String.format("Solicitacao ID %d recusada. Status alterado para RECUSADO.", id));

            enviarEmailRecusa(solicitacao.getNome(), solicitacao.getEmail());
        }, () -> {
            logService.error(String.format("Tentativa de recusar Solicitacao ID %d falhou: não encontrada.", id));
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
        logService.success(String.format("Novo Usuário ID %d criado a partir da Solicitacao ID %d. E-mail: %s.",
                usuario.getId(), solicitacao.getId(), usuario.getEmail()));

        enviarEmailAceite(usuario.getNome(), usuario.getEmail(), senhaTemporaria);
    }

    private String gerarSenhaTemporaria() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private void enviarEmailAceite(String nomeUsuario, String email, String senha) {
        String conteudoHtml = emailService.gerarEmailAceito(nomeUsuario, email, senha);
        emailService.enviarEmail(email, "Solicitação Aceita", conteudoHtml);
        logService.info(String.format("Email de ACEITE com credenciais enviado para: %s.", email));
    }

    private void enviarEmailRecusa(String nomeUsuario, String email) {
        String conteudoHtml = emailService.gerarEmailRecusado(nomeUsuario);
        emailService.enviarEmail(email, "Solicitação Recusada", conteudoHtml);
        logService.info(String.format("Email de RECUSA enviado para: %s.", email));
    }
}