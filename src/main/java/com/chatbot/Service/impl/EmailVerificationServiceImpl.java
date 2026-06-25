package com.chatbot.Service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailVerificationServiceImpl{

    private final JavaMailSender mailSender;

    public void sendVerificationEmail(
            String email,
            String token) {

        SimpleMailMessage message =
                new SimpleMailMessage();

        message.setTo(email);
        message.setSubject("Verify Your Email");

        message.setText(
                "Click the link:\n" +
                        "http://localhost:8080/api/auth/verify?token="
                        + token);

        mailSender.send(message);
    }
}
