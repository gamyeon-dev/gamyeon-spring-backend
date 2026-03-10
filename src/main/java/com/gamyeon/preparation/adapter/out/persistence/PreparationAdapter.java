package com.gamyeon.preparation.adapter.out.persistence;

import com.gamyeon.preparation.application.port.out.PreparationFilePort;
import com.gamyeon.preparation.application.port.out.PreparationPort;
import com.gamyeon.preparation.domain.Preparation;
import com.gamyeon.preparation.domain.PreparationFile;
import com.gamyeon.preparation.domain.PreparationFileType;
import com.gamyeon.preparation.domain.PreparationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PreparationAdapter implements PreparationPort, PreparationFilePort {

    private final PreparationRepository preparationRepository;
    private final PreparationFileRepository preparationFileRepository;

    @Override
    public Optional<Preparation> loadById(Long preparationId) {
        return preparationRepository.findById(preparationId);
    }

    @Override
    public Optional<Preparation> loadByIntvId(Long intvId) {
        return preparationRepository.findByIntvId(intvId);
    }

    @Override
    public Preparation save(Preparation preparation) {
        return preparationRepository.save(preparation);
    }

    @Override
    public List<PreparationFile> loadAllByPreparationId(Long preparationId) {
        return preparationFileRepository.findAllByPreparationId(preparationId);
    }

    @Override
    public boolean existsByPreparationIdAndType(Long preparationId, PreparationFileType fileType) {
        return preparationFileRepository.existsByPreparationIdAndType(preparationId, fileType);
    }

    @Override
    public PreparationFile save(PreparationFile preparationFile) {
        return preparationFileRepository.save(preparationFile);
    }
}
