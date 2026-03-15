package com.gamyeon.answer.adapter.out.persistence;

import com.gamyeon.answer.domain.Answer;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaAnswerRepository extends JpaRepository<Answer, Long> {

  Optional<Answer> findByQuestionSetId(Long questionSetId);
}
