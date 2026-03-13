package com.gamyeon.preparation.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class PreparationFile {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Long preparationId;

  @Enumerated(EnumType.STRING)
  private PreparationFileType type;

  private String originalFileName;

  private String fileKey;

  private String fileUrl;

  protected PreparationFile() {}

  private PreparationFile(
      Long preparationId,
      PreparationFileType type,
      String originalFileName,
      String fileKey,
      String fileUrl) {
    this.preparationId = preparationId;
    this.type = type;
    this.originalFileName = originalFileName;
    this.fileKey = fileKey;
    this.fileUrl = fileUrl;
  }

  public static PreparationFile create(
      Long preparationId,
      PreparationFileType type,
      String originalFileName,
      String fileKey,
      String fileUrl) {
    return new PreparationFile(preparationId, type, originalFileName, fileKey, fileUrl);
  }

  public boolean isType(PreparationFileType fileType) {
    return this.type == fileType;
  }

  public Long getId() {
    return id;
  }

  public PreparationFileType getType() {
    return type;
  }

  public String getOriginalFileName() {
    return originalFileName;
  }

  public String getFileKey() {
    return fileKey;
  }

  public String getFileUrl() {
    return fileUrl;
  }
}
