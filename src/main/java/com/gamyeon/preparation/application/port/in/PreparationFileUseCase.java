package com.gamyeon.preparation.application.port.in;

import java.util.List;

public interface PreparationFileUseCase {

  PreparationFilesRegisterResult registerFiles(List<PreparationFileCommand> commands);
}
