package com.gamyeon.intv.domain;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface IntvRepository {

    Intv save(Intv intv);

    Optional<Intv> findById(Long id);
}
