package com.boardintelligence.aspose_demo.controller;

import com.aspose.words.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/")
@Slf4j
public class AsposeDemoController {

  @GetMapping("/docx")
  public String convertFile() throws IOException {
    return convert("table");
  }

  private String convert(String filename) {
    try {
      File file = getFile(filename);
      InputStream inputStream = new FileInputStream(file);
      byte[] fileBytes = inputStream.readAllBytes();
      Document document = buildDocument(fileBytes);
      ByteArrayOutputStream outputStream = saveDocumentToOutputStream(document);
      writeToFile(outputStream, filename);
      return "ok";
    } catch (Exception e) {
      return "failed:" + e.getMessage();
    }
  }

  private File getFile(String filename) throws FileNotFoundException {
    File file = new File("testFiles/" + filename + ".docx");
    if (!file.exists()) {
      throw new FileNotFoundException("File not found at: " + file.getAbsolutePath());
    }
    return file;
  }

  private Document buildDocument(byte[] data) throws Exception {
    LoadOptions lo = new LoadOptions();
    try {
      return new Document(new ByteArrayInputStream(data), lo);
    } catch (Exception e) {
      log.error("Exception: " + e.getMessage());
      throw e;
    }
  }

  private ByteArrayOutputStream saveDocumentToOutputStream(Document document) throws Exception {
    log.debug("Saving document to output stream");
    PdfSaveOptions saveOptions = buildPdfSaveOptions();
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      document.save(outputStream, saveOptions);
      return outputStream;
    } catch (Exception e) {
      log.error("Exception in saving document to output stream: {}",
              e.getMessage());
      throw e;
    }
  }

  private PdfSaveOptions buildPdfSaveOptions() {
    PdfSaveOptions saveOptions = new PdfSaveOptions();
    saveOptions.setEmbedFullFonts(true);
    saveOptions.setImageCompression(PdfImageCompression.JPEG);
    saveOptions.setOptimizeOutput(true);
    saveOptions.setSaveFormat(SaveFormat.PDF);

    return saveOptions;
  }

  private void writeToFile(ByteArrayOutputStream baos, String filename) throws IOException {
    Path path = Paths.get("pdfs/" + filename + "-output-" + System.currentTimeMillis() + ".pdf");
    Files.write(path, baos.toByteArray());
  }

}
