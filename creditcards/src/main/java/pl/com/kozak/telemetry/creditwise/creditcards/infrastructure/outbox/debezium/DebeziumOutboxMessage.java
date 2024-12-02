package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.debezium;


import jakarta.persistence.Access;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static jakarta.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PACKAGE;

@Builder
@Getter(PACKAGE)
@Access(FIELD)
@NoArgsConstructor
@AllArgsConstructor
class DebeziumOutboxMessage {
    @Id
    private UUID id;
    private String aggregatetype;
    private String aggregateid;
    private String type;
    private String payload;
    private Instant createdAt;
    private boolean published;
    private String tracingspancontext;
    private String trace;
    private String sourceType;
    private String sourceid;
}
