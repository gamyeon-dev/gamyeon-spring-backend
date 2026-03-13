package com.gamyeon.preparation.domain;

public enum PreparationStatus {
  CREATED, // Preparation 생성 완료, 아직 질문 생성 불가
  READY, // 필수 파일(이력서) 업로드 완료, 질문 생성 가능
  QUESTION_GENERATING, // 질문 생성 요청 후 생성 진행 중
  QUESTION_GENERATED, // 질문 생성 완료
  FAILED // 질문 생성 실패
}
