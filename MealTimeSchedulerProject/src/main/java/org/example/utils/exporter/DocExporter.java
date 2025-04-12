package org.example.utils.exporter;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.example.entity.DailyPlan;

import java.io.FileOutputStream;
import java.io.IOException;

public class DocExporter {
    public static void export(DailyPlan plan, String filename) throws IOException {
        try (XWPFDocument doc = new XWPFDocument()) {
            XWPFParagraph paragraph = doc.createParagraph();
            XWPFRun run = paragraph.createRun();
            run.setText("План питания на " + plan.getDate());
            try (FileOutputStream out = new FileOutputStream(filename)) {
                doc.write(out);
            }
        }
    }
}
