package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import jakarta.persistence.Access;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.AccessType.FIELD;
import static lombok.AccessLevel.PACKAGE;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Access(FIELD)
@Getter(PACKAGE)
class Address {
    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String zip;
}
