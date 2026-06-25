package com.chatbot.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class VoiceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String audioPath;

    @Column(columnDefinition = "TEXT")
    private String transcript;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
