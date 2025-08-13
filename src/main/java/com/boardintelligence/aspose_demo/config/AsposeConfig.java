package com.boardintelligence.aspose_demo.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class AsposeConfig {
  @Value("${aspose.should-use-license}")
  private boolean shouldUseLicense;

  @Value("${aspose.public-key}")
  private String publicKey;

  @Value("${aspose.private-key}")
  private String privateKey;

  @PostConstruct
  public void init() throws IOException {
    if (shouldUseLicense) {
      setupAsposeSlides();
    }
  }

  private void setupAsposeSlides() {
    com.aspose.slides.Metered metered = new com.aspose.slides.Metered();
    metered.setMeteredKey(publicKey, privateKey);
  }

}
