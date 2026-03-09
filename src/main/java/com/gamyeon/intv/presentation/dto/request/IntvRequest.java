package com.gamyeon.intv.presentation.dto.request;

import com.gamyeon.intv.application.dto.command.CreateIntvCommand;
import com.gamyeon.intv.application.dto.command.UpdateIntvCommand;

public record IntvRequest(
        String title
) {
    public CreateIntvCommand toCommand(Long userId, String title) {
        return new CreateIntvCommand(userId, title);
    }


}