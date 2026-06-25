package com.chatbot.Service.impl;

import com.chatbot.Entity.Document;
import com.chatbot.Entity.User;
import com.chatbot.Repository.DocumentRepository;
import com.chatbot.Repository.UserRepository;
import com.chatbot.Service.DocumentService;
import com.chatbot.Service.PdfParserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DocumentServiceImpl implements DocumentService {

    private final DocumentRepository documentRepository;

    private final UserRepository userRepository;

    private final PdfParserService pdfParserService;


    @Override
    public String uploadDocument(
            MultipartFile file,
            Long userId) {

        try {

            User user = userRepository
                    .findById(userId)
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "User not found"));

            String uploadDirectory = "uploads/";

            Path uploadPath =
                    Paths.get(uploadDirectory);

            if (!Files.exists(uploadPath)) {

                Files.createDirectories(
                        uploadPath
                );
            }

            String fileName =
                    file.getOriginalFilename();

            Path filePath =
                    uploadPath.resolve(fileName);

            Files.write(
                    filePath,
                    file.getBytes()
            );
            String extractedText =
                    pdfParserService.extractText(
                            filePath.toString()
                    );
            Document document =
                    new Document();

            document.setFilename(
                    fileName
            );

            document.setFilepath(
                    filePath.toString()
            );
            document.setContent(
                    extractedText
            );
            document.setUploadedAt(LocalDateTime.now()
            );

            document.setUser(user);

            documentRepository.save(
                    document
            );

            return "Document uploaded successfully";

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to upload document",
                    e
            );
        }
    }

}

