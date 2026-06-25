package com.chatbot.Dto;

public record AuthResponse(String token, String message, long userId, long sessionId) {
}
