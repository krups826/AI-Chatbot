package com.chatbot.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String filename;

    private String filepath;

    private String documentType;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String content;

    private LocalDateTime uploadedAt;

    @ManyToOne
    private User user;
}
