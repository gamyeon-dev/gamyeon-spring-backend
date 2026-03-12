package com.gamyeon.preparation.infrastructure;

import com.gamyeon.preparation.adapter.out.storage.StorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class PreparationConfig {

    @Bean
    public S3Presigner s3Presigner(StorageProperties storageProperties) {
        return S3Presigner.builder()
                .region(storageProperties.region())
                .credentialsProvider(credentialsProvider(storageProperties))
                .build();
    }

    private AwsCredentialsProvider credentialsProvider(StorageProperties storageProperties) {
        if (StringUtils.hasText(storageProperties.accessKey()) && StringUtils.hasText(storageProperties.secretKey())) {
            return StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(storageProperties.accessKey(), storageProperties.secretKey())
            );
        }

        return DefaultCredentialsProvider.create();
    }
}
