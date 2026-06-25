package com.chatbot.Service.impl;

import com.chatbot.Dto.ChatRequest;
import com.chatbot.Dto.ChatResponse;
import com.chatbot.Entity.*;
import com.chatbot.Repository.*;
import com.chatbot.Service.ChatService;
import com.chatbot.Service.SentimentService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatClient chatClient;
    private final ChatHistoryRepository chatHistoryRepository;
    private final SessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final SentimentService sentimentService;
    private final SentimentLogRepository sentimentLogRepository;
    private final ConversionSummaryRepository conversionSummaryRepository;

    @Override
    public ChatResponse sendMessage(ChatRequest request) {
        String message = request.message() == null ? "" : request.message().trim();

        if (message.isBlank()) {
            throw new RuntimeException("Message is required");
        }

        User user = userRepository.findById(request.userId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Session session = findOrCreateSession(request.sessionId(), user);

        ConversionSummary summary =
                conversionSummaryRepository
                        .findTopByUserIdOrderByCreatedAtDesc(
                                request.userId()
                        );

        String prompt;

        if(summary != null) {

            prompt =
                    """
                    User Previous Summary:
                    %s
        
                    Current Question:
                    %s
        
                    Give a personalized response based on
                    the user's previous conversations.
                    """
                            .formatted(
                                    summary.getSummary(),
                                    message
                            );

        } else {

            prompt = message;
        }

        String aiResponse =
                chatClient.prompt()
                        .user(prompt)
                        .call()
                        .content();

        String sentiment =
                sentimentService
                        .analyzeSentiment(
                                message
                        );

        ChatHistory chatHistory = new ChatHistory();

        chatHistory.setUserMessage(message);
        chatHistory.setBotResponse(aiResponse);
        chatHistory.setTimestamp(LocalDateTime.now());
        chatHistory.setUser(user);
        chatHistory.setSession(session);

        chatHistoryRepository.save(chatHistory);

        SentimentLog sentimentLog = new SentimentLog();

        sentimentLog.setSentiment(sentiment);
        sentimentLog.setChatHistory(chatHistory);

        sentimentLogRepository.save(sentimentLog);

        return new ChatResponse(
                aiResponse,
                sentiment
        );
    }

    private Session findOrCreateSession(long requestedSessionId, User user) {
        if (requestedSessionId > 0) {
            return sessionRepository.findByIdAndUserId(requestedSessionId, user.getId())
                    .orElseGet(() -> createSession(user));
        }

        return sessionRepository.findTopByUserIdAndActiveTrueOrderByLoginTimeDesc(user.getId())
                .orElseGet(() -> createSession(user));
    }

    private Session createSession(User user) {
        Session session = new Session();

        session.setUser(user);
        session.setSessionTaken(java.util.UUID.randomUUID().toString());
        session.setLoginTime(LocalDateTime.now());
        session.setActive(true);

        return sessionRepository.save(session);
    }

    @Override
    public List<ChatResponse> getChatHistory(long userId) {
        return chatHistoryRepository
                .findByUserId(userId)
                .stream()
                .map(chat -> new ChatResponse(
                        chat.getBotResponse(),
                        chat.getSentimentLog() != null ? chat.getSentimentLog().getSentiment() : "Neutral"
                ))
                .toList();
    }

    @Override
    public void deleteChat(long chatId) {
        chatHistoryRepository.deleteById(chatId);
    }
}
