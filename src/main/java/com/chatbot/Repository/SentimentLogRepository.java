package com.chatbot.Repository;

import com.chatbot.Entity.SentimentLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface SentimentLogRepository extends JpaRepository<SentimentLog, Long> {
}