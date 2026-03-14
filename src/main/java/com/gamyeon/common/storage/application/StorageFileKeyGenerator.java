package com.gamyeon.common.storage.application;

import java.text.Normalizer;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StorageFileKeyGenerator {

  public String generate(String prefix, List<String> pathSegments, String originalFileName) {
    String normalizedPrefix = sanitizePathSegment(prefix);
    String normalizedSegments =
        pathSegments.stream()
            .map(this::sanitizePathSegment)
            .filter(segment -> !segment.isBlank())
            .collect(Collectors.joining("/"));
    String sanitizedFileName = sanitizeFileName(originalFileName);

    if (normalizedSegments.isBlank()) {
      return "%s/%s-%s".formatted(normalizedPrefix, UUID.randomUUID(), sanitizedFileName);
    }

    return "%s/%s/%s-%s"
        .formatted(normalizedPrefix, normalizedSegments, UUID.randomUUID(), sanitizedFileName);
  }

  private String sanitizePathSegment(String value) {
    if (value == null) {
      return "";
    }

    String normalized = Normalizer.normalize(value, Normalizer.Form.NFKC);
    String sanitized = normalized.replaceAll("[\\\\/:*?\"<>|\\s]+", "-");
    sanitized = sanitized.replaceAll("-{2,}", "-");
    sanitized = sanitized.replaceAll("^[.-]+|[.-]+$", "");
    return sanitized;
  }

  private String sanitizeFileName(String originalFileName) {
    String sanitized = sanitizePathSegment(originalFileName);
    return sanitized.isBlank() ? "file" : sanitized;
  }
}
