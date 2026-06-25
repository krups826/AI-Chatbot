package com.chatbot.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Memory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String memoryKey;

    @Column(columnDefinition = "TEXT")
    private String memoryValue;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
