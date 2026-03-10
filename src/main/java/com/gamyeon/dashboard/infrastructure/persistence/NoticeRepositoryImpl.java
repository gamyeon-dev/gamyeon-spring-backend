package com.gamyeon.dashboard.infrastructure.persistence;

import com.gamyeon.dashboard.application.port.outbound.NoticeRepository;
import com.gamyeon.dashboard.domain.Notice;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
