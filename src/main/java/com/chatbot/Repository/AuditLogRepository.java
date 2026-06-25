package com.chatbot.Repository;

import com.chatbot.Entity.AuditLog;
import org.springframework.data.repository.Repository;

public interface AuditLogRepository extends Repository<AuditLog, Long> {
}