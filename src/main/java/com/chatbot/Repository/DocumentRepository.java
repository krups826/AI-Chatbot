package com.chatbot.Repository;

import com.chatbot.Entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}