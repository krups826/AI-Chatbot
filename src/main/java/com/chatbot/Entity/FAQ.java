package com.chatbot.Entity;

import jakarta.persistence.*;

import javax.annotation.processing.Generated;

@Entity
public class FAQ {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String question;

    @Column(columnDefinition = "TEXT")
    private String answer;

    private String category;

    private Integer frequency;

    @ManyToOne
    @JoinColumn(name = "document_id")
    private Document document;

}
