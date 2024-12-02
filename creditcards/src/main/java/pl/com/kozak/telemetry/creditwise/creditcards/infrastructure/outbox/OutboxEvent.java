package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox;

public interface OutboxEvent {
    String getSourceId();
    String getSourceType();

    String getDestination();
    String getKey();

}
