package com.chatbot.Entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AuditLog {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String action;

        private String description;

        private LocalDateTime timestamp;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;
}

