package com.gamyeon.preparation.application.port.out;

public record StoragePresignedUrlCommand(String fileKey, String contentType) {}
