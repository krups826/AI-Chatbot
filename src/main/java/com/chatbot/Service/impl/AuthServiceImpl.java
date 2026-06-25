package com.chatbot.Service.impl;

import com.chatbot.Dto.AuthResponse;
import com.chatbot.Dto.LoginRequest;
import com.chatbot.Dto.RegisterRequest;
import com.chatbot.Entity.Session;
import com.chatbot.Entity.User;
import com.chatbot.Entity.VerificationToken;
import com.chatbot.Repository.SessionRepository;
import com.chatbot.Repository.UserRepository;
import com.chatbot.Repository.VerificationTokenRepository;
import com.chatbot.Security.JwtService;
import com.chatbot.Service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final VerificationTokenRepository verificationTokenRepository;

    private final EmailVerificationServiceImpl emailVerificationService;

    private final SessionRepository sessionRepository;

    @Override
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(
                passwordEncoder.encode(request.password())
        );
        user.setEnabled(false);

        userRepository.save(user);

        String verificationTokenValue =
                UUID.randomUUID().toString();

        VerificationToken verificationToken =
                new VerificationToken();

        verificationToken.setToken(
                verificationTokenValue
        );

        verificationToken.setUser(user);

        verificationToken.setExpiryDate(
                LocalDateTime.now().plusHours(24)
        );

        verificationTokenRepository.save(
                verificationToken
        );

        emailVerificationService.sendVerificationEmail(
                user.getEmail(),
                verificationTokenValue
        );

        String jwtToken =
                jwtService.generateToken(
                        user.getEmail()
                );

        Session session = createSession(user);

        return new AuthResponse(
                jwtToken,
                "Registration Successful",
                user.getId(),
                session.getId()
        );
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (!passwordEncoder.matches(
                request.password(),
                user.getPassword())) {

            throw new RuntimeException("Invalid Password");
        }

        String token =
                jwtService.generateToken(user.getEmail());

        Session session = createSession(user);

        return new AuthResponse(token,
                "Login Successful",
                user.getId(),
                session.getId()
        );
    }

    @Override
    public String verify(String token) {

        VerificationToken verificationToken =
                verificationTokenRepository.findByToken(token)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid Token"));

        if (verificationToken.getExpiryDate()
                .isBefore(LocalDateTime.now())) {

            throw new RuntimeException("Token Expired");
        }

        User user = verificationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        return "Email Verified Successfully";
    }

    private Session createSession(User user) {
        Session session = new Session();

        session.setUser(user);
        session.setSessionTaken(UUID.randomUUID().toString());
        session.setLoginTime(LocalDateTime.now());
        session.setActive(true);

        return sessionRepository.save(session);
    }
}
