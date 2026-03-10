package com.gamyeon.preparation.adapter.out.persistence;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvRepository;
import com.gamyeon.preparation.application.port.out.IntvPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PreparationIntvAdapter implements IntvPort {

    private final IntvRepository intvRepository;

    @Override
    public Optional<Intv> loadById(Long intvId) {
        return intvRepository.findById(intvId);
    }
}
