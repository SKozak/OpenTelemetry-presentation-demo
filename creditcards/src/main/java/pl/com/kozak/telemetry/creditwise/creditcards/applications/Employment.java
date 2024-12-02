package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import jakarta.persistence.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

import static jakarta.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PACKAGE;

@Builder
@Embeddable
@Access(FIELD)
@Getter(PACKAGE)
@NoArgsConstructor
@AllArgsConstructor
class Employment {
    @Column(nullable = false)
    private String employerName;

    @Column(nullable = false)
    private String jobTitle;

    @Column(nullable = false)
    private LocalDate employmentStartDate;
}
