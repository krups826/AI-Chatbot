package com.chatbot.Service;

import com.chatbot.Dto.AuthResponse;
import com.chatbot.Dto.LoginRequest;
import com.chatbot.Dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);


    String verify(String token);
}
