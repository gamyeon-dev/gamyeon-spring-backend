package com.gamyeon.preparation.adapter.in.web;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record PreparationFileRegisterRequest(
        @NotEmpty(message = "files는 최소 1개 이상이어야 합니다.")
        List<@Valid PreparationFileRegisterItemRequest> files
) {
}
