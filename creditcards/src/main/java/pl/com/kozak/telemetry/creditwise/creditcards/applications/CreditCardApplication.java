package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.com.kozak.telemetry.creditwise.creditcards.fraud.FraudResult;
import pl.com.kozak.telemetry.creditwise.creditcards.identity.IdentificationResult;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static jakarta.persistence.AccessType.FIELD;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;
import static pl.com.kozak.telemetry.creditwise.creditcards.applications.ApplicationState.DECLINED;

@Table
@Builder
@Getter(PACKAGE)
@Access(FIELD)
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "credit_card_application")
class CreditCardApplication {
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;
    private String cardType;
    private BigDecimal creditLimit;
    private BigDecimal annualFee;
    @Enumerated(STRING)
    private ApplicationState applicationState;
    private String applicationStateDescription;

    public Document getDocument() {
        return client.getDocument();
    }

    void decline(IdentificationResult identificationResult) {
        if (Objects.requireNonNull(identificationResult) == IdentificationResult.NOT_FOUND) {
            this.applicationState = DECLINED;
            this.applicationStateDescription = "Client document not found";
        } else if (identificationResult == IdentificationResult.NOT_ALL_DATA_MATCHED) {
            this.applicationState = DECLINED;
            this.applicationStateDescription = "Client document have different data than document in our database";
        }
    }

    String getDeclineReason() {
        return applicationStateDescription;
    }

    void decline(FraudResult analyzeResult) {
        this.applicationState = DECLINED;
        this.applicationStateDescription = analyzeResult.rejectionReason();
    }

    public String getClientFirstName() {
        return client.getName();
    }

    public String getClientLastName() {
        return client.getLastName();
    }

    public String getClientEmail() {
        return client.getEmail();
    }

    public String getClientStreet() {
        return client.getAddress().getStreet();
    }

    public LocalDate getClientBirthDate() {
        return client.getDateOfBirth();
    }

    public String getClientCity() {
        return client.getAddress().getCity();
    }

    public String getClientZipCode() {
        return client.getAddress().getZip();
    }

    public void approve() {
        this.applicationState = ApplicationState.APPROVED;
    }
}
