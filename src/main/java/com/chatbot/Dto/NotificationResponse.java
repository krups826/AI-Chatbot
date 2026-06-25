package com.chatbot.Dto;

public record NotificationResponse(Long id,
                                   String title,
                                   String message,
                                   Boolean readStatus) {
}
