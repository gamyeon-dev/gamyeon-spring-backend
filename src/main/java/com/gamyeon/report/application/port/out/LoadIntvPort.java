package com.gamyeon.report.application.port.out;

import com.gamyeon.intv.domain.Intv;
import java.util.Optional;

public interface LoadIntvPort {
  Optional<Intv> findById(Long intvId);
}
