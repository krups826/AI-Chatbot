package com.chatbot.Service.impl;

import com.chatbot.Service.PdfParserService;
import lombok.RequiredArgsConstructor;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl
        implements PdfParserService {

    @Override
    public String extractText(String filePath) {

        try {

            File file = new File(filePath);

            PDDocument document =
                    Loader.loadPDF(file);

            PDFTextStripper stripper =
                    new PDFTextStripper();

            String text =
                    stripper.getText(document);

            document.close();

            return text;

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to parse PDF",
                    e
            );
        }
    }
}