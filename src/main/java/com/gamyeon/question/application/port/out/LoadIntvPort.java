package com.gamyeon.question.application.port.out;

import com.gamyeon.intv.domain.Intv;
import java.util.Optional;

public interface LoadIntvPort {

  Optional<Intv> loadById(long id);
}
