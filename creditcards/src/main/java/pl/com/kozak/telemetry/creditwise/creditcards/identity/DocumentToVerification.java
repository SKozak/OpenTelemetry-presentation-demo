package pl.com.kozak.telemetry.creditwise.creditcards.identity;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.DocumentType;

import java.time.LocalDate;

@Getter
@Builder
@Jacksonized
public class DocumentToVerification {
    private DocumentType type;
    private String number;
    private LocalDate expiry;
}
