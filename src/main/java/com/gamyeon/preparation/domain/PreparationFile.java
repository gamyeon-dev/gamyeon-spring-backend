package com.gamyeon.preparation.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PreparationFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long preparationId;

    @Enumerated(EnumType.STRING)
    private PreparationFileType type;

    private String originalFileName;

    private String fileKey;

    private String fileUrl;

    protected PreparationFile() {
    }

    private PreparationFile(Long preparationId, PreparationFileType type, String originalFileName, String fileKey, String fileUrl) {
        this.preparationId = preparationId;
        this.type = type;
        this.originalFileName = originalFileName;
        this.fileKey = fileKey;
        this.fileUrl = fileUrl;
    }

    public static PreparationFile create(
            Long preparationId,
            PreparationFileType type,
            String originalFileName,
            String fileKey,
            String fileUrl
    ) {
        return new PreparationFile(preparationId, type, originalFileName, fileKey, fileUrl);
    }

    public boolean isType(PreparationFileType fileType) {
        return this.type == fileType;
    }
}
