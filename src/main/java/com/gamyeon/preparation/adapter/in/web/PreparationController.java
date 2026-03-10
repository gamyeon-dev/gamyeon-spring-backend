package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlResult;
import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlUseCase;
import com.gamyeon.preparation.domain.PreparationSuccessCode;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/preparations")
public class PreparationController {

    private final UploadPreparationFileUrlUseCase uploadPreparationFileUrlUseCase;

    public PreparationController(UploadPreparationFileUrlUseCase uploadPreparationFileUrlUseCase) {
        this.uploadPreparationFileUrlUseCase = uploadPreparationFileUrlUseCase;
    }

    @PostMapping("/{intvId}/files/presigned-url")
    public ResponseEntity<ApiResponse<PreparationResponse>> issueUploadUrl(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long intvId,
            @Valid @RequestBody PreparationRequest request
    ) {
        UploadPreparationFileUrlResult result = uploadPreparationFileUrlUseCase.issueUploadUrl(
                request.toCommand(userId, intvId)
        );

        return ApiResponse.success(
                PreparationSuccessCode.PREPARATION_UPLOAD_URL_ISSUED,
                PreparationResponse.from(result)
        );
    }
}
