package com.gamyeon.preparation.application.port.out;

import com.gamyeon.preparation.domain.Preparation;
import java.util.Optional;

public interface PreparationPort {
  Optional<Preparation> loadById(Long preparationId);

  Optional<Preparation> loadByIntvId(Long intvId);

  Preparation save(Preparation preparation);
}
