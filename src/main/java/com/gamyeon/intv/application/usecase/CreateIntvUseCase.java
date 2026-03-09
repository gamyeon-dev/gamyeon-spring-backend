package com.gamyeon.intv.application.usecase;

import com.gamyeon.intv.application.dto.request.CreateIntvCommand;
import com.gamyeon.intv.application.dto.response.IntvInfo;

public interface CreateIntvUseCase {

    IntvInfo create(CreateIntvCommand command);

}
