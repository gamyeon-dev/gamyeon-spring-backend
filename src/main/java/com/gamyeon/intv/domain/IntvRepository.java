package com.gamyeon.intv.domain;

import java.util.Optional;

public interface IntvRepository {

  Intv save(Intv intv);

  Optional<Intv> findById(Long id);
}
