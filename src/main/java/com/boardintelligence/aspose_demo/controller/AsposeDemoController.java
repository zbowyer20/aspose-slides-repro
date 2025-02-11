package com.boardintelligence.aspose_demo.controller;

import com.aspose.cells.Workbook;
import com.aspose.slides.LoadOptions;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@RestController
@RequestMapping("/")
@Slf4j
public class AsposeDemoController {

  @GetMapping("/pptx")
  public String pptx() {
    try {
      byte[] pptx = Files.readAllBytes(Paths.get("src/main/resources/blank-presentation.pptx"));
      Presentation presentation = buildPresentation(pptx, new LoadOptions());
      ByteArrayOutputStream outputStream = savePresentationToOutputStream(presentation);
      return "ok";
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return "broken";
    }
  }

  @GetMapping("/xlsx")
  public String xlsx() {
    try {
      byte[] xlsx = Files.readAllBytes(Paths.get("src/main/resources/blank-xlsx.xlsx"));
      Workbook workbook = new Workbook(new ByteArrayInputStream(xlsx));
      try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
        log.info("Saving workbook to output stream");
        workbook.save(outputStream, com.aspose.cells.SaveFormat.PDF);
        log.info("Workbook saved to output stream");
      }
      return "ok";
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return "broken";
    }
  }

  private Presentation buildPresentation(byte[] data, LoadOptions loadOptions) {
    try {
      return new Presentation(new ByteArrayInputStream(data), loadOptions);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw e;
    }
  }

  private ByteArrayOutputStream savePresentationToOutputStream(Presentation presentation) throws IOException {
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      log.info("Saving presentation to output stream");
      presentation.save(outputStream, SaveFormat.Pdf);
      log.info("Presentation saved to output stream");
      return outputStream;
    } catch (IOException e) {
      System.out.println("Exception: " + e.getMessage());
      throw e;
    }
  }

}
