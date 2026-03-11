package com.gamyeon.preparation.adapter.out.persistence;

import com.gamyeon.preparation.domain.PreparationFile;
import com.gamyeon.preparation.domain.PreparationFileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreparationFileRepository extends JpaRepository<PreparationFile, Long> {

    List<PreparationFile> findAllByPreparationId(Long preparationId);

    boolean existsByPreparationIdAndType(Long preparationId, PreparationFileType type);
}
