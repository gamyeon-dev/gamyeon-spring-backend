package com.gamyeon.question.domain;

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
@Table(name = "commonQuestions")
@Getter
public class CommonQuestion extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "content", columnDefinition = "text", nullable = false)
  private String content;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private CommonQuestionStatus status;
}
