package com.gamyeon.intv.presentation;

import com.gamyeon.intv.application.usecase.CreateIntvUseCase;
import com.gamyeon.intv.domain.IntvRepository;
import com.gamyeon.intv.presentation.dto.request.CreateIntvRequest;
import com.gamyeon.intv.presentation.dto.response.IntvResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class IntvController {

    private final CreateIntvUseCase createIntvUseCase;

    @PostMapping("/intvs")
    public IntvResponse create(@RequestBody CreateIntvRequest request) {
        Long userId = 1L; // 임시값
        return IntvResponse.from(
                createIntvUseCase.create(request.toCommand(userId))
        );
    }
}
