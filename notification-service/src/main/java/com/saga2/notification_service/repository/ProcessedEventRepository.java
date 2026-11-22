package com.saga2.notification_service.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProcessedEventRepository {

    private final JdbcTemplate jdbcTemplate;

    public boolean claimEvent(String eventId) {

        String sql = """
            INSERT INTO notification_schema.processed_events
                (event_id, status)
            VALUES (?, 'PROCESSING')
            ON CONFLICT (event_id) DO NOTHING
            """;

        return jdbcTemplate.update(sql, eventId) == 1;
    }

    public String findStatus(String eventId) {

        String sql = """
            SELECT status
            FROM notification_schema.processed_events
            WHERE event_id = ?
            """;

        return jdbcTemplate.query(
                sql,
                rs -> rs.next() ? rs.getString("status") : null,
                eventId
        );
    }

    public void markSent(String eventId) {

        String sql = """
            UPDATE notification_schema.processed_events
            SET status = 'SENT',
                updated_at = CURRENT_TIMESTAMP
            WHERE event_id = ?
              AND status = 'PROCESSING'
            """;

        if (jdbcTemplate.update(sql, eventId) != 1) {
            throw new IllegalStateException(
                    "Failed to mark event SENT: " + eventId
            );
        }
    }

    public void releaseFailedEvent(String eventId) {

        String sql = """
            DELETE FROM notification_schema.processed_events
            WHERE event_id = ?
              AND status = 'PROCESSING'
            """;

        jdbcTemplate.update(sql, eventId);
    }
}