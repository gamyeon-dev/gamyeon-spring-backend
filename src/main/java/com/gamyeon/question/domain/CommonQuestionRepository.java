package com.gamyeon.question.domain;

import java.util.List;

public interface CommonQuestionRepository {

  List<CommonQuestion> findRandomActiveQuestions(int count);
}
