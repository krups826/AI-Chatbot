package com.chatbot.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    @Column(unique = true)
    private String email;

    private String password;

    private String role;

    private boolean enabled;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user")
    private List<ChatHistory> chats;

    @OneToMany(mappedBy = "user")
    private List<FeedBack> feedBacks;

    @OneToMany(mappedBy = "user")
    private List<Memory> memories;

    @OneToMany(mappedBy = "user")
    private List<Session> sessions;

    @OneToMany(mappedBy = "user")
    private List<VoiceLog> voiceLogs;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<Recommendation> recommendations;

    @OneToMany(mappedBy = "user")
    private List<AuditLog> auditLogs;

    @OneToMany(mappedBy = "user")
    private List<Document> documents;
}
