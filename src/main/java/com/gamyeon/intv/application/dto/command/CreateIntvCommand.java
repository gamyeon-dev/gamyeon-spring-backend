package com.gamyeon.intv.application.dto.command;

import jakarta.validation.constraints.NotBlank;

public record CreateIntvCommand(
        Long userId,
        @NotBlank String title
        ) {
}
