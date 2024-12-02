package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.debezium;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;

@RequiredArgsConstructor
class DebeziumOutboxRepository {
    private final JdbcTemplate jdbcTemplate;


    public void save(DebeziumOutboxMessage message) {
        String sql = "INSERT INTO debezium_outbox_events (id, aggregatetype, aggregateid, type, payload, created_at, published, trace, source_type, sourceid,tracingspancontext) " +
                "VALUES (?, ?, ?, ?, ?::jsonb, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                message.getId(),
                message.getAggregatetype(),
                message.getAggregateid(),
                message.getType(),
                message.getPayload(),
                Timestamp.from(message.getCreatedAt()),
                message.isPublished(),
                message.getTrace(),
                message.getSourceType(),
                message.getSourceid(),
                message.getTracingspancontext());
    }
}
