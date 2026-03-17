// package com.gamyeon.feedback.infrastructure.persistence;
//
// import com.gamyeon.feedback.application.port.out.LoadQuestionSetPort;
// import com.gamyeon.question.domain.QuestionSet;
// import com.gamyeon.question.domain.QuestionSetRepository;
// import lombok.RequiredArgsConstructor;
// import org.springframework.stereotype.Component;
//
// import java.util.List;
// import java.util.Optional;
//
// @Component
// @RequiredArgsConstructor
// public class QuestionSetLoadAdapter implements LoadQuestionSetPort {
//
//    private final QuestionSetRepository questionSetRepository;
//
//    @Override
//    public List<QuestionSet> findAllByIntvId(Long intvId) {
//        // 기존 리포지토리에 findAllByIntvIdOrderByQuestionOrder 등이 없다면 추가 필요
//        return questionSetRepository.findAllByIntvId(intvId);
//    }
//
//    @Override
//    public Optional<QuestionSet> findById(Long id) {
//        return questionSetRepository.findById(id);
//    }
// }
