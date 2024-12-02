package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.OutboxEvent;

import java.math.BigDecimal;

record ApplicationProcessedEvent(ApplicationState applicationState, String id, Long income, String number, DocumentType type,
                                 BigDecimal creditLimit, String destination, String pesel) implements OutboxEvent {

    public static ApplicationProcessedEvent from(CreditCardApplication savedApplication, String destination) {
        return new ApplicationProcessedEvent(savedApplication.getApplicationState(),
            savedApplication.getId(),
            savedApplication.getClient().getIncome(),
            savedApplication.getClient().getDocument().getNumber(),
            savedApplication.getClient().getDocument().getType(),
            savedApplication.getCreditLimit(),
            destination,
            savedApplication.getClient().getPesel());
    }

    @Override
    public String getSourceId() {
        return id;
    }

    @Override
    public String getSourceType() {
        return "CreditCardApplication";
    }

    @Override
    public String getDestination() {
        return destination;
    }

    @Override
    public String getKey() {
        return this.pesel;
    }
}
