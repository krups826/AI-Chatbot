package com.chatbot.Controller;

import com.chatbot.Service.PdfExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class PdfExportController {

    private final PdfExportService
            pdfExportService;

    @GetMapping("/export/pdf/{userId}")
    public ResponseEntity<byte[]>
    exportPdf(
            @PathVariable Long userId
    ) {

        byte[] pdf =
                pdfExportService
                        .exportChat(userId);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=chat-history.pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }
}