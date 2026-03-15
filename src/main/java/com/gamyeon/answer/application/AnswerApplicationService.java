package com.gamyeon.answer.application;
import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlCommand;
import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlResult;
import com.gamyeon.answer.application.port.in.IssueAnswerUploadUrlUseCase;
import com.gamyeon.answer.domain.Answer;
import com.gamyeon.answer.domain.AnswerErrorCode;
import com.gamyeon.answer.domain.AnswerException;
import com.gamyeon.answer.domain.AnswerRepository;
import com.gamyeon.answer.domain.AnswerStatus;
import com.gamyeon.common.storage.application.StorageFileKeyGenerator;
import com.gamyeon.common.storage.application.port.out.StoragePresignedUrlCommand;
import com.gamyeon.common.storage.application.port.out.StoragePresignedUrlPort;
import com.gamyeon.common.storage.application.port.out.StoragePresignedUrlResult;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
@Transactional
public class AnswerApplicationService
    implements IssueAnswerUploadUrlUseCase,
        HandleAnswerSttCallbackUseCase {

  private static final Set<String> ALLOWED_EXTENSIONS = Set.of(".mp4", ".webm");
  private static final Set<String> ALLOWED_CONTENT_TYPE_PREFIXES =
      Set.of("video/mp4", "video/webm");
  private final AnswerRepository answerRepository;
  private final StorageFileKeyGenerator storageFileKeyGenerator;
  private final StoragePresignedUrlPort storagePresignedUrlPort;
  public AnswerApplicationService(
      AnswerRepository answerRepository,
      StorageFileKeyGenerator storageFileKeyGenerator,
      StoragePresignedUrlPort storagePresignedUrlPort,
      AnswerAnalysisProperties answerAnalysisProperties) {
    this.answerRepository = answerRepository;
    this.storageFileKeyGenerator = storageFileKeyGenerator;
    this.storagePresignedUrlPort = storagePresignedUrlPort;
  }

  @Override
  @Transactional(readOnly = true)
  public IssueAnswerUploadUrlResult issueUploadUrl(IssueAnswerUploadUrlCommand command) {
    validateVideoFile(command.originalFileName(), command.contentType());
    validateFileSize(command.fileSizeBytes());

    String fileKey =
        storageFileKeyGenerator.generate(
            "answers",
            List.of(String.valueOf(command.questionSetId()), "video"),
            command.originalFileName());
    StoragePresignedUrlResult result =
        storagePresignedUrlPort.createUploadUrl(
            new StoragePresignedUrlCommand(fileKey, command.contentType()));

    return new IssueAnswerUploadUrlResult(
        command.questionSetId(),
        command.originalFileName(),
        result.fileKey(),
        result.presignedUrl(),
        result.fileUrl(),
        result.expiresInSeconds());
  }
  private void validateVideoFile(String originalFileName, String contentType) {
    if (originalFileName == null || !hasAllowedExtension(originalFileName)) {
      throw new AnswerException(AnswerErrorCode.INVALID_FILE_EXTENSION);
    }

    if (!hasAllowedContentType(contentType)) {
      throw new AnswerException(AnswerErrorCode.INVALID_CONTENT_TYPE);
    }
  }

  private boolean hasAllowedExtension(String originalFileName) {
    String normalized = originalFileName.toLowerCase();
    return ALLOWED_EXTENSIONS.stream().anyMatch(normalized::endsWith);
  }

  private boolean hasAllowedContentType(String contentType) {
    if (contentType == null) {
      return false;
    }

    String normalized = contentType.toLowerCase();
    return ALLOWED_CONTENT_TYPE_PREFIXES.stream().anyMatch(normalized::startsWith);
  }

  private void validateFileSize(Long fileSizeBytes) {
    if (fileSizeBytes == null || fileSizeBytes < 1) {
      throw new AnswerException(AnswerErrorCode.INVALID_FILE_SIZE);
    }
  }
}
