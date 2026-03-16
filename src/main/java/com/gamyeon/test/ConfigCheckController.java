package com.gamyeon.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConfigCheckController {

  @Value("${storage.s3.bucket}")
  private String secret;

  @GetMapping("/check-config")
  public String check() {
    return "Loaded secret: " + secret;
  }
}
