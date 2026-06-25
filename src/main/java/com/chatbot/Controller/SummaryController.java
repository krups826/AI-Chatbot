package com.chatbot.Controller;

import com.chatbot.Service.SummaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class SummaryController {

    private final SummaryService summaryService;

    @PostMapping("/summary/{userId}")
    public String generateSummary(@PathVariable Long userId) {

        return summaryService.generateSummary(userId);

    }
}
