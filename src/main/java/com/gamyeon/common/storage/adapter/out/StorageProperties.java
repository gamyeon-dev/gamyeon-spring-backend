package com.gamyeon.common.storage.adapter.out;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage.s3")
public record StorageProperties(
    String bucket,
    String region,
    long presignedDurationSeconds,
    String accessKey,
    String secretKey) {}
