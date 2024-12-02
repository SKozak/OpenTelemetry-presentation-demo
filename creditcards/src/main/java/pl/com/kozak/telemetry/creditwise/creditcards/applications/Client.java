package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

import static jakarta.persistence.AccessType.FIELD;

@Entity
@Builder
@Access(FIELD)
@Table(name = "client")
@NoArgsConstructor
@AllArgsConstructor
@Getter(AccessLevel.PACKAGE)
class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String pesel;
    private String lastName;

    private String email;

    private LocalDate dateOfBirth;

    @Embedded
    private Address address;

    @Column(nullable = false)
    private Long income;

    @Embedded
    private Document document;

    @Embedded
    private Employment employment;

    public Document getDocument() {
        return document;
    }
}
