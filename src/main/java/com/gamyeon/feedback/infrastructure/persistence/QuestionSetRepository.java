package com.gamyeon.feedback.infrastructure.persistence;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuestionSetRepository extends JpaRepository<QuestionSetEntity, Long> {

  @Query("SELECT qs.intvId FROM QuestionSetEntity qs WHERE qs.id = :id")
  Optional<Long> findIntvIdById(@Param("id") Long id);
}
