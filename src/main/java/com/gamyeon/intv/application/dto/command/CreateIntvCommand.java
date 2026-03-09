package com.gamyeon.intv.application.dto.request;

public record CreateIntvCommand(
        Long userId,
        String title
) {
}
