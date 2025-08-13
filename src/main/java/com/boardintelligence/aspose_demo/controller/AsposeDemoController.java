package com.boardintelligence.aspose_demo.controller;

import com.aspose.slides.IWarningCallback;
import com.aspose.slides.IWarningInfo;
import com.aspose.slides.LoadOptions;
import com.aspose.slides.PdfOptions;
import com.aspose.slides.Presentation;
import com.aspose.slides.ReturnAction;
import com.aspose.slides.SaveFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
@RestController
@RequestMapping("/")
public class AsposeDemoController {

  @GetMapping("/pptx")
  public String pptx() {
    try {
      File file = getFile("presentation-with-images");
      InputStream inputStream = new FileInputStream(file);
      byte[] pptx = inputStream.readAllBytes();
      Presentation presentation = buildPresentation(pptx, new LoadOptions());
      try {
        String outputPath = savePresentationToFile(presentation);
        return "PDF saved to: " + outputPath;
      } finally {
        if (presentation != null) {
          presentation.dispose();
        }
      }
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      return "broken";
    }
  }
  private File getFile(String filename) throws FileNotFoundException {
    File file = new File("/testFiles/" + filename + ".pptx");
    if (!file.exists()) {
      throw new FileNotFoundException("File not found.");
    }
    return file;
  }

  private Presentation buildPresentation(byte[] data, LoadOptions loadOptions) {
    try {
      return new Presentation(new ByteArrayInputStream(data), loadOptions);
    } catch (Exception e) {
      System.out.println("Error: " + e.getMessage());
      throw e;
    }
  }

  private String savePresentationToFile(Presentation presentation) throws IOException {
    String outputPath = "output/presentation.pdf";
    try {
      Files.createDirectories(Paths.get("output"));
      PdfOptions pdfOptions = buildPdfOptions();
      System.out.println("Saving presentation to file: " + outputPath);
      presentation.save(outputPath, SaveFormat.Pdf, pdfOptions);
      System.out.println("Presentation saved to file: " + outputPath);
      return outputPath;
    } catch (IOException e) {
      System.out.println("Exception: " + e.getMessage());
      throw e;
    }
  }

  private PdfOptions buildPdfOptions() {
    PdfOptions pdfOptions = new PdfOptions();
    pdfOptions.setWarningCallback(new HandleWarnings());

    return pdfOptions;
  }

  class HandleWarnings implements IWarningCallback {
    public int warning(IWarningInfo warning) {
      System.out.println("Data loss warning - " + warning.getDescription());
      return ReturnAction.Continue;
    }
  }

}
