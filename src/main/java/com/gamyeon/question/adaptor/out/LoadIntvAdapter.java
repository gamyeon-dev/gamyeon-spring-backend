package com.gamyeon.question.adaptor.out;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvRepository;
import com.gamyeon.question.application.port.out.LoadIntvPort;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class LoadIntvAdapter implements LoadIntvPort {

  private final IntvRepository intvRepository;

  @Override
  public Optional<Intv> loadById(long id) {
    return intvRepository.findById(id);
  }
}
