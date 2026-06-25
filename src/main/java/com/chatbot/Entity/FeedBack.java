package com.chatbot.Entity;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.ISBN;

import java.time.LocalDateTime;

@Entity
public class FeedBack {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private Integer rating;

    private String comment;

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
