package com.gamyeon.intv.presentation.dto.request;

import com.gamyeon.intv.application.dto.request.CreateIntvCommand;

public record CreateIntvRequest(
        String title
) {
    public CreateIntvCommand toCommand(Long userId) {
        return new CreateIntvCommand(userId, title);
    }
}