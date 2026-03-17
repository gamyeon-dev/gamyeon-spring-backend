package com.gamyeon.report.infrastructure.external;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvRepository;
import com.gamyeon.report.application.port.out.LoadIntvPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class IntvLoadAdapter implements LoadIntvPort {

  private final IntvRepository intvRepository;

  @Override
  public Optional<Intv> findById(Long intvId) {
    return intvRepository.findById(intvId);
  }
}
