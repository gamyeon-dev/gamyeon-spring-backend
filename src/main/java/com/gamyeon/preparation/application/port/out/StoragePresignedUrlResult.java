package com.gamyeon.preparation.application.port.out;

public record StoragePresignedUrlResult(
        String fileKey,
        String presignedUrl,
        String fileUrl,
        long expiresInSeconds
) {
}
