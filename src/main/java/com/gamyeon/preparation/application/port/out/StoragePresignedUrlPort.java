package com.gamyeon.preparation.application.port.out;

public interface StoragePresignedUrlPort {

  StoragePresignedUrlResult createUploadUrl(StoragePresignedUrlCommand command);
}
