package com.gamyeon.dashboard.infrastructure.persistence;

import com.gamyeon.dashboard.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaNoticeRepository extends JpaRepository<Notice, Long> {
}
