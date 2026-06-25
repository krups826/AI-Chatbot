package com.chatbot.Dto;

public record AnalyticsResponse(Integer totalChats,
                                Integer totalUsers,
                                String mostAskedQuestion) {
}
