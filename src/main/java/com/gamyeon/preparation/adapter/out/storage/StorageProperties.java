package com.gamyeon.preparation.adapter.out.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;
import software.amazon.awssdk.regions.Region;

@ConfigurationProperties(prefix = "storage.s3")
public record StorageProperties(
        String bucket,
        Region region,
        long presignedDurationSeconds,
        String accessKey,
        String secretKey
) {
}
