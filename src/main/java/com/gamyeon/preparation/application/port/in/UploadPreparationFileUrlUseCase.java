package com.gamyeon.preparation.application.port.in;

public interface UploadPreparationFileUrlUseCase {

  UploadPreparationFileUrlResult issueUploadUrl(UploadPreparationFileUrlCommand command);
}
