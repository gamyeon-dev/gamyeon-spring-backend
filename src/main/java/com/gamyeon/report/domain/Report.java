package com.gamyeon.report.domain;

import com.gamyeon.common.entity.BaseEntity;
import jakarta.persistence.*;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "reports", uniqueConstraints = @UniqueConstraint(columnNames = "intv_id"))
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "intv_id", nullable = false, unique = true)
  private Long intvId;

  @Column(name = "user_id", nullable = false)
  private Long userId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ReportStatus status;

  @Column(name = "job_category")
  private String jobCategory; // MVP1: null, MVP2 직군 선택

  @Column(name = "total_score")
  private Integer totalScore;

  @Column(name = "answered_count")
  private Integer answeredCount;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<String> strengths;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(columnDefinition = "jsonb")
  private List<String> weaknesses;

  @JdbcTypeCode(SqlTypes.JSON)
  @Column(name = "report_data", columnDefinition = "jsonb")
  private Map<String, Object> reportData;

  // ── 팩토리 메서드 ──────────────────────────────────────────────────────────

  public static Report createInProgress(Long intvId, Long userId) {
    Report report = new Report();
    report.intvId = intvId;
    report.userId = userId;
    report.status = ReportStatus.IN_PROGRESS;
    return report;
  }

  // ── 상태 전이 메서드 ────────────────────────────────────────────────────────

  public void complete(
      int totalScore,
      int answeredCount,
      List<String> strengths,
      List<String> weaknesses,
      Map<String, Object> reportData) {
    this.totalScore = totalScore;
    this.answeredCount = answeredCount;
    this.strengths = strengths;
    this.weaknesses = weaknesses;
    this.reportData = reportData;
    this.status = ReportStatus.SUCCEED;
  }

  public void fail() {
    this.status = ReportStatus.FAILED;
  }
}
