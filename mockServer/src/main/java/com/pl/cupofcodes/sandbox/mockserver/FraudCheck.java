package com.pl.cupofcodes.sandbox.mockserver;

import java.time.LocalDate;

public record FraudCheck(String name, String email, String lastName, LocalDate dateOfBirth, String street, String city, String zip) {
}
