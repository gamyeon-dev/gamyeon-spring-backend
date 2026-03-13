package com.gamyeon.preparation.application.port.out;

import com.gamyeon.preparation.domain.PreparationFile;
import com.gamyeon.preparation.domain.PreparationFileType;
import java.util.List;

public interface PreparationFilePort {

  List<PreparationFile> loadAllByPreparationId(Long preparationId);

  boolean existsByPreparationIdAndType(Long preparationId, PreparationFileType fileType);

  PreparationFile save(PreparationFile preparationFile);
}
