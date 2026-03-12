package com.gamyeon.intv.application.usecase;

import com.gamyeon.intv.application.dto.command.CreateIntvCommand;
import com.gamyeon.intv.application.dto.result.IntvInfo;

public interface CreateUseCase {

    IntvInfo create(CreateIntvCommand command);

}
