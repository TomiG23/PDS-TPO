package com.example.notification.adapter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class JavaMailEmailClientAdapter implements EmailClient {
    private final String username;
    private final String password;
    private final Session session;

    /**
     * Constructor que configura la sesión de JavaMail con Gmail SMTP
     * @param username Tu dirección de Gmail (ej: tytoapache@gmail.com)
     * @param password Tu App Password de Gmail (ej: cquzsqalpflvswxa)
     */
    public JavaMailEmailClientAdapter(String username, String password) {
        this.username = username;
        this.password = password;

        // Configuración de propiedades para Gmail SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Crear sesión con autenticación
        this.session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    @Override
    public void send(String to, String subject, String body) {
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            
            // Email enviado silenciosamente (sin mensaje en consola)
        } catch (MessagingException e) {
            // Error silencioso - podrías activar esto para debugging:
            // System.err.println("[Email ERROR] No se pudo enviar email a " + to + ": " + e.getMessage());
        }
    }
}

