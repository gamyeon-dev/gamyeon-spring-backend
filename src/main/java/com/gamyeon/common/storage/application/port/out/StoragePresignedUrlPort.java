package com.gamyeon.common.storage.application.port.out;

public interface StoragePresignedUrlPort {

  StoragePresignedUrlResult createUploadUrl(StoragePresignedUrlCommand command);
}
