package com.ticketsystem.shared.infrastructure.email;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            log.info("Email sent successfully to: {}", to);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", to, e);
            throw new EmailServiceException("Failed to send email", e);
        }
    }

    public void sendPasswordResetEmail(String to, String resetUrl) {
        String subject = "Recuperação de Senha - Sistema de Ingressos";
        String text = String.format(
            "Olá,\n\n" +
            "Você solicitou a recuperação de senha. Clique no link abaixo para redefinir sua senha:\n\n" +
            "%s\n\n" +
            "Este link expira em 15 minutos.\n\n" +
            "Se você não solicitou esta recuperação, ignore este email.\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Compra de Ingressos",
            resetUrl
        );
        sendEmail(to, subject, text);
    }

    public void sendPurchaseConfirmationEmail(String to, String purchaseCode, String eventName, int quantity) {
        String subject = "Compra Confirmada - Sistema de Ingressos";
        String text = String.format(
            "Olá,\n\n" +
            "Sua compra foi confirmada com sucesso!\n\n" +
            "Detalhes da compra:\n" +
            "- Código: %s\n" +
            "- Evento: %s\n" +
            "- Quantidade: %d ingresso(s)\n\n" +
            "Atenciosamente,\n" +
            "Sistema de Compra de Ingressos",
            purchaseCode, eventName, quantity
        );
        sendEmail(to, subject, text);
    }

    public static class EmailServiceException extends RuntimeException {
        public EmailServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}

