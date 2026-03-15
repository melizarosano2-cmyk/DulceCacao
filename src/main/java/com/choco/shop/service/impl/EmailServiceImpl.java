package com.choco.shop.service.impl;

import com.choco.shop.entity.User;
import com.choco.shop.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.base.url}")
    private String baseUrl;

    @Override
    public void sendVerificationEmail(User user, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Verifica tu cuenta - Dulce Cacao");

        String verificationUrl = baseUrl + "/verify-email?token=" + token;

        message.setText("Hola " + user.getFullName() + ",\n\n" +
                "Gracias por registrarte en Dulce Cacao.\n\n" +
                "Por favor, verifica tu dirección de correo electrónico haciendo clic en el siguiente enlace:\n\n" +
                verificationUrl + "\n\n" +
                "Este enlace es válido por 24 horas.\n\n" +
                "Si no creaste esta cuenta, puedes ignorar este correo.\n\n" +
                "Saludos,\n" +
                "Equipo Dulce Cacao");

        mailSender.send(message);
    }

    @Override
    public void sendPasswordResetEmail(User user, String token) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(user.getEmail());
        message.setSubject("Recuperación de contraseña - Dulce Cacao");

        String resetUrl = baseUrl + "/reset-password?token=" + token;

        message.setText("Hola " + user.getFullName() + ",\n\n" +
                "Recibimos una solicitud para restablecer tu contraseña.\n\n" +
                "Haz clic en el siguiente enlace para crear una nueva contraseña:\n\n" +
                resetUrl + "\n\n" +
                "Este enlace es válido por 1 hora.\n\n" +
                "Si no solicitaste restablecer tu contraseña, puedes ignorar este correo.\n\n" +
                "Saludos,\n" +
                "Equipo Dulce Cacao");

        mailSender.send(message);
    }
}
