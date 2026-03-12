package com.gamyeon.preparation.application.port.in;

import java.util.List;

public interface PreparationFileUseCase {

    PreparationFileRegisterResult registerFiles(Long userId, Long intvId, List<PreparationFileCommand> commands);
}
