package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.manual;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

import static jakarta.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PACKAGE;

@Table
@Builder
@Getter(PACKAGE)
@Access(FIELD)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "outbox_events")
class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sourceId;
    private String tracingData;
    private String sourceType;
    private String destination;
    private String payload;
    private Instant createdAt;
    private boolean published;
    private String key;

    public void markPublished() {
        this.published = true;
    }
}
