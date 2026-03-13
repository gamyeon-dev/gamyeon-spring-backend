package com.gamyeon.preparation.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreparationRepository extends JpaRepository<Preparation, Long> {

  Optional<Preparation> findByIntvId(Long intvId);
}
