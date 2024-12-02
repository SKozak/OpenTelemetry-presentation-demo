package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import jakarta.persistence.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.AccessType.FIELD;
import static jakarta.persistence.EnumType.STRING;
import static lombok.AccessLevel.PACKAGE;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Access(FIELD)
@Getter(PACKAGE)
class Document {
    @Column(name = "document_type", nullable = false)
    @Enumerated(STRING)
    private DocumentType type;

    @Column(name = "document_number", nullable = false)
    private String number;

    @Column(name = "document_expiry", nullable = false)
    private LocalDate expiry;
}
