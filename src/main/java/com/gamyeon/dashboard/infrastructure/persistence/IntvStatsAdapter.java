package com.gamyeon.dashboard.infrastructure.persistence;

import com.gamyeon.dashboard.application.port.inbound.DailyStat;
import com.gamyeon.dashboard.application.port.outbound.IntvStatsPort;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Component
public class IntvStatsAdapter implements IntvStatsPort {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<DailyStat> findDailyStats(Long userId, LocalDate startDate, LocalDate endDate) {
        String sql = """
                SELECT CAST(finished_at AS DATE) AS date, COUNT(*) AS count
                FROM intv
                WHERE user_id = :userId
                  AND status = 'FINISHED'
                  AND CAST(finished_at AS DATE) BETWEEN :startDate AND :endDate
                GROUP BY CAST(finished_at AS DATE)
                ORDER BY CAST(finished_at AS DATE)
                """;

        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);

        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        return rows.stream()
                .map(row -> new DailyStat(
                        ((Date) row[0]).toLocalDate(),
                        ((Number) row[1]).longValue()
                ))
                .toList();
    }
}
