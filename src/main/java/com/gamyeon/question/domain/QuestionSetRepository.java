package com.gamyeon.question.domain;

import java.util.List;

public interface QuestionSetRepository {

  void saveAll(List<QuestionSet> questionSets);

  List<QuestionSet> getAllByIntvId(Long intvId);
}
