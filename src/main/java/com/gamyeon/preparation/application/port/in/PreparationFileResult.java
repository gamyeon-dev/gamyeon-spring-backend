package com.gamyeon.preparation.application.port.in;

import com.gamyeon.preparation.domain.PreparationFileType;

public record PreparationFileResult(
    Long fileId, PreparationFileType fileType, String originalFileName, String fileUrl) {}
