package com.chatbot.Service;

import com.chatbot.Dto.ChatRequest;
import com.chatbot.Dto.ChatResponse;
import com.chatbot.Dto.SentimentResponse;

import java.util.List;

public interface ChatService {

    ChatResponse sendMessage(ChatRequest request);

    List<ChatResponse> getChatHistory(long userId);

    void deleteChat(long chatId);


}
