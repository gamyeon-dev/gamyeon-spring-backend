package com.gamyeon.intv.application;

import com.gamyeon.intv.application.dto.command.CreateIntvCommand;
import com.gamyeon.intv.application.dto.command.ChangeStateIntvCommand;
import com.gamyeon.intv.application.dto.command.UpdateIntvCommand;
import com.gamyeon.intv.application.dto.result.IntvInfo;
import com.gamyeon.intv.application.usecase.ChangeStateUseCase;
import com.gamyeon.intv.application.usecase.CreateUseCase;
import com.gamyeon.intv.application.usecase.UpdateTitleUseCase;
import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvErrorCode;
import com.gamyeon.intv.domain.IntvException;
import com.gamyeon.intv.domain.IntvRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IntvApplicationService implements CreateUseCase, ChangeStateUseCase, UpdateTitleUseCase {

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

    @Override
    public IntvInfo updateTitle(UpdateIntvCommand command) {

        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        intv.updateTitle(command.title());
        return IntvInfo.from(intv);
    }

    @Override
    public void start(ChangeStateIntvCommand command) {
        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        intv.start();
    }

    @Override
    public void pause(ChangeStateIntvCommand command) {
        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        intv.pause();
    }

    @Override
    public void resume(ChangeStateIntvCommand command) {
        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        intv.resume();
    }

    @Override
    public void finish(ChangeStateIntvCommand command) {
        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        intv.finish();
    }


    private Intv getOwnedIntv(Long userId, Long intvId) {
        Intv intv = intvRepository.findById(intvId)
                .orElseThrow(() -> new IntvException(IntvErrorCode.INTV_NOT_FOUND));

        if (!intv.getUserId().equals(userId)) {
            throw new IntvException(IntvErrorCode.INTV_FORBIDDEN);
        }

        return intv;
    }
}