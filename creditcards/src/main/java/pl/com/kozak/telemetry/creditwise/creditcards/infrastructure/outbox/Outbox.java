package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox;

public interface Outbox {
    void save(OutboxEvent outboxEvent);
}
