package com.gamyeon.intv.application.usecase;

import com.gamyeon.intv.application.dto.command.UpdateIntvCommand;
import com.gamyeon.intv.application.dto.result.IntvInfo;

public interface UpdateTitleUseCase {

    IntvInfo updateTitle(UpdateIntvCommand command);
}
