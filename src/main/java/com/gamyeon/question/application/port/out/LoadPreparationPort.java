package com.gamyeon.question.application.port.out;

public interface LoadPreparationPort {
    // 이거쓸때(주입받을때) 파이썬API 호출
    // 질문생성 요청을 위한 면접자료 가져오기(이걸 파이썬api 호출할때 Request로 넘겨주면 됨
    PreparationForQuestionGeneration loadByIntvId(Long intvId);
}

