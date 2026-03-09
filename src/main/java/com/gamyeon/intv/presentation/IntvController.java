package com.gamyeon.intv.presentation;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.intv.application.dto.command.ChangeStateIntvCommand;
import com.gamyeon.intv.application.dto.command.UpdateIntvCommand;
import com.gamyeon.intv.application.dto.result.IntvInfo;
import com.gamyeon.intv.application.usecase.ChangeStateUseCase;
import com.gamyeon.intv.application.usecase.CreateUseCase;
import com.gamyeon.intv.application.usecase.UpdateTitleUseCase;
import com.gamyeon.intv.domain.IntvSuccessCode;
import com.gamyeon.intv.presentation.dto.request.IntvRequest;
import com.gamyeon.intv.presentation.dto.response.IntvResponse;
import com.gamyeon.user.application.port.inbound.UserInfo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/intvs")
public class IntvController {

    private final CreateUseCase createUseCase;
    private final ChangeStateUseCase changeStateUseCase;
    private final UpdateTitleUseCase updateTitleUseCase;

    public IntvController(CreateUseCase createUseCase, ChangeStateUseCase changeStateUseCase, UpdateTitleUseCase updateTitleUseCase) {
        this.createUseCase = createUseCase;
        this.changeStateUseCase = changeStateUseCase;
        this.updateTitleUseCase = updateTitleUseCase;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<IntvResponse>> create(@AuthenticationPrincipal Long userId,
                                                            @RequestBody IntvRequest request) {

        IntvInfo info = createUseCase.create(request.toCommand(userId, request.title()));

        return ApiResponse.success(IntvSuccessCode.INTV_CREATED, IntvResponse.from(info));
    }

    @PostMapping("/intvs")
    public IntvResponse create(@RequestBody CreateIntvRequest request) {
        Long userId = 1L; // 임시값
        return IntvResponse.from(
                createIntvUseCase.create(request.toCommand(userId))
        );
    }
}
