package com.boardintelligence.aspose_demo.controller;

import com.aspose.slides.FontsLoader;
import com.aspose.slides.IWarningCallback;
import com.aspose.slides.IWarningInfo;
import com.aspose.slides.LoadOptions;
import com.aspose.slides.PdfOptions;
import com.aspose.slides.Presentation;
import com.aspose.slides.ReturnAction;
import com.aspose.slides.SaveFormat;
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
import java.nio.file.StandardCopyOption;

@RestController
@RequestMapping("/")
@Slf4j
public class AsposeDemoController {

  @GetMapping("/pptx/wingdings")
  public String convertFileWithWingdings() throws IOException {
    copyFont("Wingdings");
    FontsLoader.loadExternalFonts(new String[]{"/fonts"});
    //    FontsLoader.clearCache();
    return convert("Wingdings");
  }

  @GetMapping("/pptx/consolas")
  public String convertFileWithConsolas() throws IOException {
    copyFont("Consolas");
    FontsLoader.loadExternalFonts(new String[]{"/fonts"});
    //    FontsLoader.clearCache();
    return convert("Consolas");
  }

  private void copyFont(String fontName) throws IOException{
    Path source = Paths.get("/fonts-external/" + fontName + ".ttf");
    Path target = Paths.get("/fonts/" + fontName + ".ttf");
    try {
      Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
    } catch (IOException e) {
      log.error("Exception: " + e.getMessage());
      throw e;
    }
  }

  private String convert(String filename) {
    try {
      File file = getFile(filename);
      InputStream inputStream = new FileInputStream(file);
      byte[] pptx = inputStream.readAllBytes();
      // Arrays.stream(FontsLoader.getFontFolders()).forEach(System.out::println);
      Presentation presentation = buildPresentation(pptx);
      ByteArrayOutputStream outputStream = savePresentationToOutputStream(presentation);
      writeToFile(outputStream, filename);
      return "ok";
    } catch (Exception e) {
      return "failed:" + e.getMessage();
    }
  }

  private File getFile(String filename) throws FileNotFoundException {
    File file = new File("/testFiles/" + filename + ".pptx");
    if (!file.exists()) {
      throw new FileNotFoundException("File not found.");
    }
    return file;
  }

  private Presentation buildPresentation(byte[] data) {
    LoadOptions lo = new LoadOptions();
    lo.setWarningCallback(new HandleFontsWarnings());
    try {
      return new Presentation(new ByteArrayInputStream(data), lo);
    } catch (Exception e) {
      log.error("Exception: " + e.getMessage());
      throw e;
    }
  }

  private ByteArrayOutputStream savePresentationToOutputStream(Presentation presentation) throws IOException {
    PdfOptions po = new PdfOptions();
    po.setWarningCallback(new HandleFontsWarnings());
    try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
      log.info("Saving presentation to output stream");
      presentation.save(outputStream, SaveFormat.Pdf, po);
      log.info("Presentation saved to output stream");
      return outputStream;
    } catch (IOException e) {
      log.error("Exception: " + e.getMessage());
      throw e;
    }
  }

  private void writeToFile(ByteArrayOutputStream baos, String filename) throws IOException {
    Path path = Paths.get("/pdfs/" + filename + "-output-" + System.currentTimeMillis() + ".pdf");
    Files.write(path, baos.toByteArray());
  }

//  private static final List<String> FONT_EXTENSIONS = Arrays.asList(".ttf", ".otf", ".ttc", ".pfb", ".pfm", ".fon");
//
//  public void printFontFiles() {
//    // Directories to search for font files
////    String[] directories = {"/usr/share/fonts", "/usr/local/share/fonts", "fonts", "/root/.fonts"};
//    String[] directories = {"fonts"};
//
//    for (String dir : directories) {
//      System.out.println("Scanning directory: " + dir);
//      Path startPath = Paths.get(dir);
//      if (!Files.exists(startPath)) {
//        System.out.println("Directory does not exist: " + dir);
//        continue;
//      }
//      try {
//        Files.walk(startPath)
//                .forEach(System.out::println);
//      } catch (IOException e) {
//        System.err.println("Error scanning directory " + dir + ": " + e.getMessage());
//      }
//      System.out.println();
//    }
//  }
//
//  private static boolean hasFontExtension(Path path) {
//    String fileName = path.getFileName().toString().toLowerCase();
//    return FONT_EXTENSIONS.stream().anyMatch(fileName::endsWith);
//  }

  class HandleFontsWarnings implements IWarningCallback
  {
    public int warning(IWarningInfo warning)
    {
      log.warn(
              warning.getWarningType() + " - " + warning.getDescription()); // "Font will be substituted from X to Y"
      return ReturnAction.Continue;
    }
  }

}
