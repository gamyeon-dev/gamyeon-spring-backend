package com.gamyeon.dashboard.infrastructure.persistence;

import com.gamyeon.dashboard.application.port.outbound.NoticeRepository;
import com.gamyeon.dashboard.domain.Notice;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class NoticeRepositoryImpl implements NoticeRepository {

  private final JpaNoticeRepository jpaNoticeRepository;

  public NoticeRepositoryImpl(JpaNoticeRepository jpaNoticeRepository) {
    this.jpaNoticeRepository = jpaNoticeRepository;
  }

  @Override
  public List<Notice> findAll() {
    return jpaNoticeRepository.findAll();
  }

  @Override
  public Optional<Notice> findById(Long id) {
    return jpaNoticeRepository.findById(id);
  }
}
