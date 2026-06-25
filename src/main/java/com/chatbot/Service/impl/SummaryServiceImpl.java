package com.chatbot.Service.impl;

import com.chatbot.Entity.ChatHistory;
import com.chatbot.Entity.ConversionSummary;
import com.chatbot.Entity.User;
import com.chatbot.Repository.ChatHistoryRepository;
import com.chatbot.Repository.ConversionSummaryRepository;
import com.chatbot.Repository.UserRepository;
import com.chatbot.Service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.operator.ContentVerifier;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SummaryServiceImpl implements SummaryService {

    private final ChatHistoryRepository chatHistoryRepository;
    private final ChatClient chatClient;
    private final UserRepository userRepository;
    private final ConversionSummaryRepository conversionSummaryRepository;

    @Override
    public String generateSummary(Long userId){
    List<ChatHistory>chats = chatHistoryRepository.findByUserId(userId);

    StringBuilder conversation = new StringBuilder();

        for(ChatHistory chat : chats){

        conversation.append("User: ")
                .append(chat.getUserMessage())
                .append("\n");

        conversation.append("AI: ")
                .append(chat.getBotResponse())
                .append("\n");
    }
    String prompt =
            "Summarize the following conversation:\n\n"
                    + conversation;

    String summary = chatClient.prompt()
                    .user(prompt)
                    .call()
                    .content();

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        ConversionSummary conversationSummary =
                new ConversionSummary();

        conversationSummary.setSummary(summary);
        conversationSummary.setCreatedAt(LocalDateTime.now());
        conversationSummary.setUser(user);

        conversionSummaryRepository
                .save(conversationSummary);

        return summary;
}
}
