package com.gamyeon.answer.domain;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository {

  Answer save(Answer answer);

  Optional<Answer> findById(Long answerId);

  Optional<Answer> findByQuestionSetId(Long questionSetId);

  List<Answer> findAllByIntvId(Long intvId);
}
