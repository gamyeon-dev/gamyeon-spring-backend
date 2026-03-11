package com.gamyeon.preparation.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PreparationRepository extends JpaRepository<Preparation, Long> {

    Optional<Preparation> findByIntvId(Long intvId);
}