package com.gamyeon.intv.application;

import com.gamyeon.intv.application.dto.request.CreateIntvCommand;
import com.gamyeon.intv.application.dto.response.IntvInfo;
import com.gamyeon.intv.application.usecase.CreateIntvUseCase;
import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntvApplicationService implements CreateIntvUseCase {

    private final IntvRepository intvRepository;

    public IntvApplicationService(IntvRepository intvRepository) {
        this.intvRepository = intvRepository;
    }

    @Override
    public IntvInfo create(CreateIntvCommand command) {
        Intv intv = Intv.create(command.userId(), command.title());
        Intv savedIntv = intvRepository.save(intv);
        return IntvInfo.from(savedIntv);
    }
}