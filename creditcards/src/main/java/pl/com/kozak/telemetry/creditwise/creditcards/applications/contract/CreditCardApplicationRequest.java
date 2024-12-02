package pl.com.kozak.telemetry.creditwise.creditcards.applications.contract;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;

@Data
@Validated
public class CreditCardApplicationRequest {
    @NotNull
    private Client client;
    @NotNull
    private String cardType;
    @NotNull
    private BigDecimal requestedCardLimit;

    @Data
    public static class Client {
        @NotNull
        private String name;
        private String lastName;
        private String email;
        @NotNull
        private String pesel;
        @NotNull
        private String birthDate;
        private Address address;
        @NotNull
        private Long income;
        @NotNull
        private Document document;
        private Employment employment;

        @Data
        public static class Address {
            @NotNull
            private String street;
            @NotNull
            private String city;
            @NotNull
            private String country;
            @NotNull
            private String postalCode;
        }

        @Data
        public static class Document {
            @NotNull
            private String type;
            @NotNull
            private String number;
            private String expiry;
        }

        @Data
        public static class Employment {
            @NotNull
            private String employerName;
            @NotNull
            private String jobTitle;
            @NotNull
            private String startDate;
        }

    }
}
