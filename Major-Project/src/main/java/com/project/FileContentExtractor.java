package com.project;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

public class FileContentExtractor {

    // Method to extract content from a .txt file
    public static String extractTxtContent(File file) throws IOException {
        return new String(Files.readAllBytes(file.toPath()));
    }

    // Method to extract content from a .pdf file
    public static String extractPdfContent(File file) throws IOException {
        try (PDDocument document = PDDocument.load(file)) {
            PDFTextStripper pdfStripper = new PDFTextStripper();
            return pdfStripper.getText(document);
        }
    }

    // Method to extract content from a .doc or .docx file
    public static String extractDocContent(File file) throws IOException {
        String fileName = file.getName();
        if (fileName.endsWith(".doc")) {
            try (FileInputStream fis = new FileInputStream(file);
                 HWPFDocument document = new HWPFDocument(fis);
                 WordExtractor extractor = new WordExtractor(document)) {
                return extractor.getText();
            }
        } else if (fileName.endsWith(".docx")) {
            try (FileInputStream fis = new FileInputStream(file);
                 XWPFDocument document = new XWPFDocument(fis);
                 XWPFWordExtractor extractor = new XWPFWordExtractor(document)) {
                return extractor.getText();
            }
        }
        return "";
    }
}