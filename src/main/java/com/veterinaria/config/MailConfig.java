package com.veterinaria.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import jakarta.annotation.PostConstruct;
import java.util.Properties;

/**
 * Configuración explícita de JavaMailSender
 * Necesaria para Railway donde la autoconfiguration puede fallar
 */
@Configuration
@Slf4j
public class MailConfig {

    @Value("${spring.mail.host:smtp.gmail.com}")
    private String host;

    @Value("${spring.mail.port:587}")
    private int port;

    @Value("${spring.mail.username:}")
    private String username;

    @Value("${spring.mail.password:}")
    private String password;

    @Value("${app.mail.enabled:true}")
    private boolean mailEnabled;

    @PostConstruct
    public void logMailConfig() {
        log.info("═══════════════════════════════════════════════════════════");
        log.info("           CONFIGURACIÓN DE EMAIL CARGADA                  ");
        log.info("═══════════════════════════════════════════════════════════");
        log.info("Host: {}", host);
        log.info("Port: {}", port);
        log.info("Username: {}", username);
        log.info("Password: {}", password != null && !password.isEmpty() ? "****CONFIGURADO****" : "⚠️ NO CONFIGURADO");
        log.info("Mail Enabled: {}", mailEnabled);
        log.info("═══════════════════════════════════════════════════════════");

        if (username == null || username.isEmpty()) {
            log.error("⚠️ MAIL_USERNAME no está configurado!");
        }
        if (password == null || password.isEmpty()) {
            log.error("⚠️ MAIL_PASSWORD no está configurado!");
        }
    }

    @Bean
    @Primary
    public JavaMailSender javaMailSender() {
        log.info("Creando JavaMailSender Bean...");

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(username);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");
        props.put("mail.smtp.writetimeout", "10000");
        props.put("mail.debug", "true");

        log.info("JavaMailSender configurado correctamente");
        return mailSender;
    }
}