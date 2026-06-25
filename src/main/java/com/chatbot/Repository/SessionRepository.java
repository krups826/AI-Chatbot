package com.chatbot.Repository;

import com.chatbot.Entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {

    Optional<Session> findTopByUserIdAndActiveTrueOrderByLoginTimeDesc(long userId);

    Optional<Session> findByIdAndUserId(long id, long userId);
}
