package com.chatbot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class SentimentLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String sentiment;

    @Column(columnDefinition = "TEXT")
    private String message;

    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private ChatHistory chat;

    @OneToOne
    @JoinColumn(name = "chat_history_id")
    private ChatHistory chatHistory;
}
