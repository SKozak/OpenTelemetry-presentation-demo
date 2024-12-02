package pl.com.kozak.telemetry.creditwise.fraud.check;

import java.time.LocalDate;

public record FraudCheck(String name, String email, String lastName, LocalDate dateOfBirth, String street, String city, String zip) {
}
