package com.chatbot.Repository;

import com.chatbot.Entity.FAQ;
import org.springframework.data.repository.Repository;

public interface FAQRepository extends Repository<FAQ, Long> {

    void findByQuestion(String question);
}