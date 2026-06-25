package com.chatbot.Service;

import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    String uploadDocument(
            MultipartFile file,
            Long userId);
}
