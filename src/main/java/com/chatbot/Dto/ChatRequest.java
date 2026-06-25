package com.chatbot.Dto;

public record ChatRequest(String message, long userId,long sessionId) {
}
