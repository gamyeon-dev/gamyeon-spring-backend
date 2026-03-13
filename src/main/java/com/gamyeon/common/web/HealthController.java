package com.gamyeon.common.web;

import com.gamyeon.common.response.SuccessResponse;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

  @GetMapping("/health")
  public ResponseEntity<SuccessResponse<Map<String, String>>> health() {
    return ResponseEntity.ok(SuccessResponse.of(Map.of("status", "UP")));
  }
}
