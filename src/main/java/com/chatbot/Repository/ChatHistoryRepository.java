package com.chatbot.Repository;

import com.chatbot.Entity.ChatHistory;
import org.springframework.data.repository.Repository;

import java.util.Arrays;
import java.util.List;

public interface ChatHistoryRepository extends Repository<ChatHistory, Long> {
    List<ChatHistory> findByUserId(long userId);

    void deleteById(long chatId);

    ChatHistory save(ChatHistory chatHistory);
}