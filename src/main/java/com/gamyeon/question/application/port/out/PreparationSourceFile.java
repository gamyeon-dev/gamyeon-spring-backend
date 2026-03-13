package com.gamyeon.question.application.port.out;

import com.gamyeon.preparation.domain.PreparationFileType;

public record PreparationSourceFile(PreparationFileType fileType, String fileKey) {}
