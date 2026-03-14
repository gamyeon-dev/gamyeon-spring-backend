package com.gamyeon.common.storage.adapter.out;

import com.gamyeon.common.storage.application.port.out.StoragePresignedUrlCommand;
import com.gamyeon.common.storage.application.port.out.StoragePresignedUrlPort;
import com.gamyeon.common.storage.application.port.out.StoragePresignedUrlResult;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

@Component
@RequiredArgsConstructor
public class S3StoragePresignedUrlAdapter implements StoragePresignedUrlPort {

  private final S3Presigner s3Presigner;
  private final StorageProperties storageProperties;

  @Override
  public StoragePresignedUrlResult createUploadUrl(StoragePresignedUrlCommand command) {
    PutObjectRequest putObjectRequest =
        PutObjectRequest.builder()
            .bucket(storageProperties.bucket())
            .key(command.fileKey())
            .contentType(command.contentType())
            .build();

    Duration expiration = Duration.ofSeconds(storageProperties.presignedDurationSeconds());
    PutObjectPresignRequest presignRequest =
        PutObjectPresignRequest.builder()
            .signatureDuration(expiration)
            .putObjectRequest(putObjectRequest)
            .build();

    PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);

    return new StoragePresignedUrlResult(
        command.fileKey(),
        presignedRequest.url().toString(),
        createFileUrl(command.fileKey()),
        expiration.getSeconds());
  }

  private String createFileUrl(String fileKey) {
    return "https://%s.s3.%s.amazonaws.com/%s"
        .formatted(storageProperties.bucket(), storageProperties.region(), fileKey);
  }
}
