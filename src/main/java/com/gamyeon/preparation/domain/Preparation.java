package com.gamyeon.preparation.domain;

import com.gamyeon.common.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.util.List;

@Entity
@Getter
public class Preparation extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long intvId;

    @Enumerated(EnumType.STRING)
    private PreparationStatus status;

    protected Preparation() {
    }

    private Preparation(Long intvId, PreparationStatus status) {
        this.intvId = intvId;
        this.status = status;
    }

    public static Preparation create(Long intvId) {
        return new Preparation(intvId, PreparationStatus.CREATED);
    }

    public void markReady() {
        if (this.status != PreparationStatus.CREATED && this.status != PreparationStatus.FAILED) {
            throw new PreparationException(PreparationErrorCode.DO_NOT_READY);
        }
        this.status = PreparationStatus.READY;
    }

    public void startQuestionGeneration() {
        if (this.status != PreparationStatus.READY) {
            throw new PreparationException(PreparationErrorCode.DO_NOT_GENERATING);
        }
        this.status = PreparationStatus.QUESTION_GENERATING;
    }

    public void completeQuestionGeneration() {
        if (this.status != PreparationStatus.QUESTION_GENERATING) {
            throw new PreparationException(PreparationErrorCode.DO_NOT_GENERATED);
        }
        this.status = PreparationStatus.QUESTION_GENERATED;
    }

    public void failQuestionGeneration() {
        if (this.status != PreparationStatus.QUESTION_GENERATING) {
            throw new PreparationException(PreparationErrorCode.DO_NOT_FAILED);
        }
        this.status = PreparationStatus.FAILED;
    }

    public boolean satisfiesRequiredFiles(List<PreparationFile> files) {
        return files.stream()
                .anyMatch(file -> file.isType(PreparationFileType.RESUME));
    }

    public Long getId() {
        return id;
    }

    public Long getIntvId() {
        return intvId;
    }

    public PreparationStatus getStatus() {
        return status;
    }
}
