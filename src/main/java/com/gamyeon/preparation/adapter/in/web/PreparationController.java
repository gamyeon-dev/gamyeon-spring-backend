package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.common.response.ApiResponse;
import com.gamyeon.preparation.application.port.in.PreparationFileRegisterResult;
import com.gamyeon.preparation.application.port.in.PreparationFileUseCase;
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

    private final PreparationFileUseCase preparationFileUseCase;
    private final UploadPreparationFileUrlUseCase uploadPreparationFileUrlUseCase;

    public PreparationController(
            PreparationFileUseCase preparationFileUseCase,
            UploadPreparationFileUrlUseCase uploadPreparationFileUrlUseCase
    ) {
        this.preparationFileUseCase = preparationFileUseCase;
        this.uploadPreparationFileUrlUseCase = uploadPreparationFileUrlUseCase;
    }

    @PostMapping("/{intvId}/files")
    public ResponseEntity<ApiResponse<PreparationFileRegisterResponse>> registerFile(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long intvId,
            @Valid @RequestBody PreparationFileRegisterRequest request
    ) {
        PreparationFileRegisterResult result = preparationFileUseCase.registerFile(
                request.toCommand(userId, intvId)
        );

        return ApiResponse.success(
                PreparationSuccessCode.PREPARATION_FILE_REGISTERED,
                PreparationFileRegisterResponse.from(result)
        );
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
