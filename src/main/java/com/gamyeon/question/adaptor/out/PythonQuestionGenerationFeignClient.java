package com.gamyeon.question.adaptor.out;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "pythonQuestionGenerationClient", url = "${ai.server.url}")
public interface PythonQuestionGenerationFeignClient {

    @PostMapping("/internal/v1/questions/generate")
    void requestGeneration(@RequestBody AiQuestionGenerationRequest request);
}
