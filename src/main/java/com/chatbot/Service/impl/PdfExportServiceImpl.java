package com.chatbot.Service.impl;

import com.chatbot.Entity.ChatHistory;
import com.chatbot.Repository.ChatHistoryRepository;
import com.chatbot.Service.PdfExportService;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportServiceImpl  implements PdfExportService {

    private final ChatHistoryRepository chatHistoryRepository;

    @Override
    public byte[] exportChat(Long userId) {

        List<ChatHistory> chats = chatHistoryRepository.findByUserId(userId);

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter.getInstance(
                    document,
                    outputStream
            );

            document.open();

            document.add(
                    new Paragraph(
                            "Chat History Report"
                    )
            );

            document.add(
                    new Paragraph(" ")
            );

            for(ChatHistory chat : chats) {

                document.add(
                        new Paragraph(
                                "User: "
                                        + chat.getUserMessage()
                        )
                );

                document.add(
                        new Paragraph(
                                "AI: "
                                        + chat.getBotResponse()
                        )
                );

                document.add(
                        new Paragraph(
                                "----------------------"
                        )
                );
            }

            document.close();

            return outputStream.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "PDF generation failed"
            );
        }
    }
}
