package com.gamyeon.question.adaptor.in.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.question.adaptor.in.dto.request.CallBackRequest;
import com.gamyeon.question.application.port.in.CreateQuestionSetUseCase;
import com.gamyeon.question.domain.QuestionSuccessCode;
import com.gamyeon.question.infrastucture.QuestionSetRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AiCallBackController {

    private final CreateQuestionSetUseCase createQuestionSetUseCase;
    private final QuestionSetRepositoryAdapter questionSetRepositoryAdapter;

    @PostMapping("internal/v1/questions/callback")
    public ResponseEntity<ApiResponse<Void>> callback(@RequestBody CallBackRequest callBack) {

        createQuestionSetUseCase.create(callBack.toCommand());

        return ApiResponse.success(QuestionSuccessCode.CREATE_SUCCESS);
    }
}
