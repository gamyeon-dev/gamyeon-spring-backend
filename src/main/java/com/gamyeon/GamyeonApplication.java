package com.gamyeon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GamyeonApplication {

  public static void main(String[] args) {
    SpringApplication.run(GamyeonApplication.class, args);
  }
}
