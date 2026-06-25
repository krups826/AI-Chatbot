package com.chatbot.Controller;

import com.chatbot.Dto.AuthResponse;
import com.chatbot.Dto.LoginRequest;
import com.chatbot.Dto.RegisterRequest;
import com.chatbot.Entity.User;
import com.chatbot.Service.AuthService;
import com.chatbot.Service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request){
        return authService.register(request);
    }
    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        return authService.login(request);
    }

    @GetMapping("/verify")
    public String verify(
            @RequestParam String token) {

        return authService.verify(token);
    }
}
