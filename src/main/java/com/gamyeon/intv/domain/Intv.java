package com.gamyeon.intv.domain;

import com.gamyeon.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Entity
public class Intv extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String title;

    @Enumerated(EnumType.STRING)
    private IntvStatus status;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private LocalDateTime pausedAt;

    private Long durationSeconds;

    @Column(nullable = false)
    private Long totalPausedSeconds;

    protected Intv() {
    }

    private Intv(Long userId, String title) {
        this.userId = userId;
        this.title = title;
        this.status = IntvStatus.READY;
        this.totalPausedSeconds = 0L;
    }

    // 생성
    public static Intv create(Long userId, String title) {
        return new Intv(userId, title);
    }

    // 제목 수정
    public void updateTitle(String title) {
        this.title = title;
    }

    // 시작
    public void start() {
        this.startedAt = LocalDateTime.now();
        this.status = IntvStatus.IN_PROGRESS;
    }

    // 중단
    public void pause() {
        if (status != IntvStatus.IN_PROGRESS || pausedAt != null) {
            throw new IntvException(IntvErrorCode.DO_NOT_PAUSE);
        }
        this.pausedAt = LocalDateTime.now();
        this.status = IntvStatus.PAUSED;
    }

    // 재개
    public void resume() {
        if (status != IntvStatus.PAUSED || pausedAt == null) {
            throw new IntvException(IntvErrorCode.DO_NOT_RESUME);
        }
        long pausedSeconds = Duration.between(pausedAt, LocalDateTime.now()).toSeconds();
        this.totalPausedSeconds += pausedSeconds;
        this.pausedAt = null;
        this.status = IntvStatus.IN_PROGRESS;
    }

    // 종료
    public void finish() {
        if (status != IntvStatus.IN_PROGRESS) {
            throw new IntvException(IntvErrorCode.DO_NOT_FINISH);
        }
        LocalDateTime now = LocalDateTime.now();
        this.finishedAt = now;
        long totalElapsedSeconds = // 총 경과 시간(시작시각 - 현재시각)
                Duration.between(startedAt, now).getSeconds();
        long actualDurationSeconds = // 실제 소요된 시간(총 경과시간 - 총 중단 시간)
                totalElapsedSeconds - totalPausedSeconds;
        this.durationSeconds = actualDurationSeconds;
        this.status = IntvStatus.FINISHED;
        // TODO : 중단 시간 음수 검증, 중단된 면접 종료 가능하게 할 지 정책 검토

    }

}
