package com.project.extension.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void enviarEmail(String para, String assunto, String conteudoHtml) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(para);
            helper.setSubject(assunto);
            helper.setText(conteudoHtml, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Falha ao enviar email", e);
        }
    }

    public String gerarEmailAceito(String nomeUsuario, String email, String senha, String cargo) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Solicitação Aceita</title>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; background: #e6f2ff; margin:0; padding:0; }" +
                "  .container { max-width: 600px; margin: 30px auto; background: rgba(255, 255, 255, 0.85);" +
                "    padding: 20px; border-radius: 12px; box-shadow: 0 8px 16px rgba(0,0,0,0.1); }" +
                "  h1 { color: #007acc; text-align:center; }" +
                "  p { color: #004080; font-size: 16px; }" +
                "  .credenciais { background: #cce6ff; padding: 10px; border-radius: 8px; margin-top: 15px; }" +
                "  .footer { text-align:center; font-size:12px; color:#666; margin-top:20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Bem-vindo à Vidraçaria!</h1>" +
                "<p>Olá <b>" + nomeUsuario + "</b>, sua solicitação foi aprovada.</p>" +
                "<div class='credenciais'>" +
                "<p><b>Email:</b> " + email + "<br>" +
                "<b>Senha temporária:</b> " + senha + "<br>" +
                "<b>Cargo:</b> " + cargo + "</p>" +
                "</div>" +
                "<p>Lembre-se de alterar sua senha no primeiro acesso.</p>" +
                "<div class='footer'>© 2025 Vidraçaria Daryo. Todos os direitos reservados.</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

    public String gerarEmailRecusado(String nomeUsuario) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "<meta charset='UTF-8'>" +
                "<title>Solicitação Recusada</title>" +
                "<style>" +
                "  body { font-family: Arial, sans-serif; background: #e6f2ff; margin:0; padding:0; }" +
                "  .container { max-width: 600px; margin: 30px auto; background: rgba(255, 255, 255, 0.85);" +
                "    padding: 20px; border-radius: 12px; box-shadow: 0 8px 16px rgba(0,0,0,0.1); }" +
                "  h1 { color: #007acc; text-align:center; }" +
                "  p { color: #004080; font-size: 16px; }" +
                "  .footer { text-align:center; font-size:12px; color:#666; margin-top:20px; }" +
                "</style>" +
                "</head>" +
                "<body>" +
                "<div class='container'>" +
                "<h1>Solicitação Recusada</h1>" +
                "<p>Olá <b>" + nomeUsuario + "</b>, infelizmente sua solicitação foi recusada.</p>" +
                "<div class='footer'>© 2025 Vidraçaria Daryo. Todos os direitos reservados.</div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}