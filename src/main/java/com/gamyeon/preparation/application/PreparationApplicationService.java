package com.gamyeon.preparation.application;

import com.gamyeon.intv.domain.Intv;
import com.gamyeon.intv.domain.IntvErrorCode;
import com.gamyeon.intv.domain.IntvException;
import com.gamyeon.preparation.application.port.in.PreparationFileCommand;
import com.gamyeon.preparation.application.port.in.PreparationFileResult;
import com.gamyeon.preparation.application.port.in.PreparationFileUseCase;
import com.gamyeon.preparation.application.port.in.PreparationResult;
import com.gamyeon.preparation.application.port.in.PreparationUseCase;
import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlCommand;
import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlResult;
import com.gamyeon.preparation.application.port.in.UploadPreparationFileUrlUseCase;
import com.gamyeon.preparation.application.port.out.IntvPort;
import com.gamyeon.preparation.application.port.out.PreparationFilePort;
import com.gamyeon.preparation.application.port.out.PreparationPort;
import com.gamyeon.preparation.application.port.out.StoragePresignedUrlCommand;
import com.gamyeon.preparation.application.port.out.StoragePresignedUrlPort;
import com.gamyeon.preparation.application.port.out.StoragePresignedUrlResult;
import com.gamyeon.preparation.domain.Preparation;
import com.gamyeon.preparation.domain.PreparationErrorCode;
import com.gamyeon.preparation.domain.PreparationException;
import com.gamyeon.preparation.domain.PreparationFile;
import com.gamyeon.preparation.domain.PreparationFileType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
@Transactional
public class PreparationApplicationService implements
        PreparationUseCase,
        PreparationFileUseCase,
        UploadPreparationFileUrlUseCase {

    private static final String PDF_EXTENSION = ".pdf";
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    private final IntvPort intvPort;
    private final PreparationPort preparationPort;
    private final PreparationFilePort preparationFilePort;
    private final StoragePresignedUrlPort storagePresignedUrlPort;

    public PreparationApplicationService(
            IntvPort intvPort,
            PreparationPort preparationPort,
            PreparationFilePort preparationFilePort,
            StoragePresignedUrlPort storagePresignedUrlPort
    ) {
        this.intvPort = intvPort;
        this.preparationPort = preparationPort;
        this.preparationFilePort = preparationFilePort;
        this.storagePresignedUrlPort = storagePresignedUrlPort;
    }

    @Override
    public void create(Long intvId) {
        Preparation preparation = Preparation.create(intvId);
        preparationPort.save(preparation);
    }

    @Override
    @Transactional(readOnly = true)
    public PreparationResult get(Long userId, Long intvId) {
        Intv intv = getOwnedIntv(userId, intvId);
        Preparation preparation = getPreparationByIntvId(intv.getId());

        List<PreparationFileResult> fileResults = preparationFilePort.loadAllByPreparationId(preparation.getId()).stream()
                .map(file -> new PreparationFileResult(
                        file.getId(),
                        file.getType(),
                        file.getOriginalFileName(),
                        file.getFileUrl()
                ))
                .toList();

        return new PreparationResult(
                preparation.getId(),
                preparation.getIntvId(),
                preparation.getStatus(),
                fileResults
        );
    }

    @Override
    public void registerFile(PreparationFileCommand command) {
        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        Preparation preparation = getPreparationByIntvId(intv.getId());

        validateDuplicateFileType(preparation.getId(), command.fileType());

        PreparationFile preparationFile = PreparationFile.create(
                preparation.getId(),
                command.fileType(),
                command.originalFileName(),
                command.fileKey(),
                command.fileUrl()
        );

        preparationFilePort.save(preparationFile);

        List<PreparationFile> files = preparationFilePort.loadAllByPreparationId(preparation.getId());
        if (preparation.satisfiesRequiredFiles(files)) {
            preparation.markReady();
            preparationPort.save(preparation);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public UploadPreparationFileUrlResult issueUploadUrl(UploadPreparationFileUrlCommand command) {
        Intv intv = getOwnedIntv(command.userId(), command.intvId());
        Preparation preparation = getPreparationByIntvId(intv.getId());

        validateDuplicateFileType(preparation.getId(), command.fileType());
        validatePdfFile(command.originalFileName(), command.contentType());
        validateFileSize(command.fileType(), command.fileSizeBytes());

        String fileKey = createFileKey(preparation.getId(), command.fileType(), command.originalFileName());
        StoragePresignedUrlResult result = storagePresignedUrlPort.createUploadUrl(new StoragePresignedUrlCommand(
                fileKey,
                command.contentType()
        ));

        return new UploadPreparationFileUrlResult(
                preparation.getId(),
                command.fileType(),
                command.originalFileName(),
                result.fileKey(),
                result.presignedUrl(),
                result.fileUrl(),
                result.expiresInSeconds()
        );
    }

    private Intv getOwnedIntv(Long userId, Long intvId) {
        Intv intv = intvPort.loadById(intvId)
                .orElseThrow(() -> new IntvException(IntvErrorCode.INTV_NOT_FOUND));

        if (!intv.getUserId().equals(userId)) {
            throw new IntvException(IntvErrorCode.INTV_FORBIDDEN);
        }

        return intv;
    }

    private Preparation getPreparationByIntvId(Long intvId) {
        return preparationPort.loadByIntvId(intvId)
                .orElseThrow(() -> new PreparationException(PreparationErrorCode.PREPARATION_NOT_FOUND));
    }

    private void validateDuplicateFileType(Long preparationId, PreparationFileType fileType) {
        boolean exists = preparationFilePort.existsByPreparationIdAndType(preparationId, fileType);
        if (exists) {
            throw new PreparationException(PreparationErrorCode.PREPARATION_FILE_TYPE_ALREADY_EXISTS);
        }
    }

    private void validatePdfFile(String originalFileName, String contentType) {
        String normalizedFileName = originalFileName.toLowerCase(Locale.ROOT);
        if (!normalizedFileName.endsWith(PDF_EXTENSION)) {
            throw new PreparationException(PreparationErrorCode.INVALID_FILE_EXTENSION);
        }

        if (!PDF_CONTENT_TYPE.equalsIgnoreCase(contentType)) {
            throw new PreparationException(PreparationErrorCode.INVALID_CONTENT_TYPE);
        }
    }

    private void validateFileSize(PreparationFileType fileType, Long fileSizeBytes) {
        if (fileSizeBytes > fileType.maxFileSizeBytes()) {
            throw new PreparationException(PreparationErrorCode.FILE_SIZE_EXCEEDED);
        }
    }

    private String createFileKey(Long preparationId, PreparationFileType fileType, String originalFileName) {
        String sanitizedFileName = sanitizeFileName(originalFileName);
        String fileTypePath = fileType.name().toLowerCase(Locale.ROOT);
        return "preparations/%d/%s/%s-%s".formatted(
                preparationId,
                fileTypePath,
                UUID.randomUUID(),
                sanitizedFileName
        );
    }

    private String sanitizeFileName(String originalFileName) {
        String normalized = Normalizer.normalize(originalFileName, Normalizer.Form.NFKC);
        String sanitized = normalized.replaceAll("[\\\\/:*?\"<>|\\s]+", "-");
        sanitized = sanitized.replaceAll("-{2,}", "-");
        sanitized = sanitized.replaceAll("^[.-]+|[.-]+$", "");
        return sanitized.isBlank() ? "file" : sanitized;
    }
}
