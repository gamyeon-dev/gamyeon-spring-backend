package com.gamyeon.report.infrastructure.persistence;

import com.gamyeon.report.application.port.out.LoadReportPort;
import com.gamyeon.report.application.port.out.SaveReportPort;
import com.gamyeon.report.domain.Report;
import com.gamyeon.report.domain.ReportStatus;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReportPersistenceAdapter implements LoadReportPort, SaveReportPort {

  private final ReportJpaRepository repository;

  @Override
  public Report save(Report report) {
    return repository.save(report);
  }

  @Override
  public boolean existsByIntvId(Long intvId) {
    return repository.existsByIntvId(intvId);
  }

  @Override
  public Optional<Report> findByIntvId(Long intvId) {
    return repository.findByIntvId(intvId);
  }

  @Override
  public Optional<Report> findByIntvIdWithLock(Long intvId) {
    return repository.findByIntvIdWithLock(intvId);
  }

  @Override
  public List<Report> findAllTimedOut(ReportStatus status, LocalDateTime threshold) {
    return repository.findAllByStatusAndCreatedAtBefore(status, threshold);
  }

  @Override
  public List<Report> findAllByUserId(Long userId) {
    return repository.findAllByUserId(userId);
  }
}
