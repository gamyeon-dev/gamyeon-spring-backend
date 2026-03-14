package com.gamyeon.common.storage.application.port.out;

public record StoragePresignedUrlCommand(String fileKey, String contentType) {}
