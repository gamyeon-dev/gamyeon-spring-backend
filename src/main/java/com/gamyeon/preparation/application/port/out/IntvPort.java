package com.gamyeon.preparation.application.port.out;

import com.gamyeon.intv.domain.Intv;
import java.util.Optional;

public interface IntvPort {

  Optional<Intv> loadById(Long intvId);
}
