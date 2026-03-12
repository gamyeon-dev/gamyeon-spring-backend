package com.gamyeon.preparation.adapter.in.web;

import com.gamyeon.preparation.application.port.in.PreparationFileRegisterResult;
import com.gamyeon.preparation.application.port.in.PreparationFileResult;
import com.gamyeon.preparation.domain.PreparationStatus;

import java.util.List;

public record PreparationFileRegisterResponse(
        Long preparationId,
        PreparationStatus preparationStatus,
        List<PreparationFileItemResponse> files
) {

    public static PreparationFileRegisterResponse from(PreparationFileRegisterResult result) {
        return new PreparationFileRegisterResponse(
                result.preparationId(),
                result.preparationStatus(),
                result.files().stream()
                        .map(PreparationFileItemResponse::from)
                        .toList()
        );
    }

    public record PreparationFileItemResponse(
            Long fileId,
            com.gamyeon.preparation.domain.PreparationFileType fileType,
            String originalFileName,
            String fileUrl
    ) {
        public static PreparationFileItemResponse from(PreparationFileResult result) {
            return new PreparationFileItemResponse(
                    result.fileId(),
                    result.fileType(),
                    result.originalFileName(),
                    result.fileUrl()
            );
        }
    }
}
