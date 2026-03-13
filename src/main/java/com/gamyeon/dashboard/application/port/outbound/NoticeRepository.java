package com.gamyeon.dashboard.application.port.outbound;

import com.gamyeon.dashboard.domain.Notice;
import java.util.List;
import java.util.Optional;

public interface NoticeRepository {
  List<Notice> findAll();

  Optional<Notice> findById(Long id);
}
