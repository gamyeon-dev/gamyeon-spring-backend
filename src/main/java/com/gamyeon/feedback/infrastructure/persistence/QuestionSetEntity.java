package com.gamyeon.feedback.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "QUESTION_SETS")
public class QuestionSetEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "intv_id", nullable = false)
  private Long intvId;

  @Column(name = "content", nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(name = "question_order")
  private Integer questionOrder;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  protected QuestionSetEntity() {}

  public Long getIntvId() {
    return intvId;
  }
}
