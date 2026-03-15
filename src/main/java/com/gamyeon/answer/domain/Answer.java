package com.gamyeon.answer.domain;

import com.gamyeon.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "answers")
@Getter
public class Answer extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private Long questionSetId;

  @Column(nullable = false)
  private String originalFileName;

  @Column(nullable = false)
  private String fileKey;

  @Column(nullable = false)
  private String fileUrl;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Long fileSizeBytes;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 30)
  private AnswerStatus status;

  @Column(columnDefinition = "TEXT")
  private String content;

  @Column(columnDefinition = "TEXT")
  private String errorMessage;

  protected Answer() {}

  private Answer(
      Long questionSetId,
      String originalFileName,
      String fileKey,
      String fileUrl,
      String contentType,
      Long fileSizeBytes) {
    this.questionSetId = questionSetId;
    this.originalFileName = originalFileName;
    this.fileKey = fileKey;
    this.fileUrl = fileUrl;
    this.contentType = contentType;
    this.fileSizeBytes = fileSizeBytes;
    this.status = AnswerStatus.UPLOADED;
  }

  public static Answer create(
      Long questionSetId,
      String originalFileName,
      String fileKey,
      String fileUrl,
      String contentType,
      Long fileSizeBytes) {
    return new Answer(
        questionSetId, originalFileName, fileKey, fileUrl, contentType, fileSizeBytes);
  }

  public void markSttProcessing() {
    this.status = AnswerStatus.STT_PROCESSING;
    this.errorMessage = null;
  }

  public void completeStt(String content) {
    this.status = AnswerStatus.STT_COMPLETED;
    this.content = content;
    this.errorMessage = null;
  }

  public void failStt(String errorMessage) {
    this.status = AnswerStatus.STT_FAILED;
    this.errorMessage = errorMessage;
  }
}
