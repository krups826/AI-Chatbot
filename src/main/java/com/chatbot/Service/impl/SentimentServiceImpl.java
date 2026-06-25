package com.chatbot.Service.impl;

import com.chatbot.Entity.SentimentLog;
import com.chatbot.Repository.SentimentLogRepository;
import com.chatbot.Service.SentimentService;
import org.springframework.stereotype.Service;

@Service
public class SentimentServiceImpl implements SentimentService {

    @Override
    public String analyzeSentiment(String message){
        message = message.toLowerCase();

        if(message.contains("happy") || message.contains("good") || message.contains("great")
            || message.contains("excellent")){
            return "Positive";
        }

        return "Neutral";
    }
}
