package org.example.utils.exporter;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.example.entity.DailyPlan;

import java.io.IOException;

public class PdfExporter {
    public static void export(DailyPlan plan, String filename) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                content.beginText();
                content.setFont(PDType1Font.HELVETICA_BOLD, 12);
                content.newLineAtOffset(50, 700);
                content.showText("План питания на " + plan.getDate());
                content.endText();
            }

            document.save(filename);
        }
    }
}