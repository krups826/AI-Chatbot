package com.chatbot.Controller;

import com.chatbot.Dto.ChatRequest;
import com.chatbot.Dto.ChatResponse;
import com.chatbot.Service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(
            @RequestBody ChatRequest request) {

        return chatService.sendMessage(request);
    }

    @GetMapping("/history/{userId}")
    public List<ChatResponse> getHistory(
            @PathVariable Long userId) {

        return chatService.getChatHistory(userId);
    }
    @DeleteMapping("{chatId}")
    void deleteChat(@PathVariable Long chatId){
            chatService.deleteChat(chatId);
    }
}
