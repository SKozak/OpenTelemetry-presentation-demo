package pl.com.kozak.telemetry.creditwise.fraud.check;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document
@AllArgsConstructor
public class ReportedFraud {
    @Id
    private String id;
    private String name;
    private String email;
    private String lastName;
    private LocalDate dateOfBirth;
    private String street;
    private String city;
    private String zip;
    private FraudStatus fraudStatus;
    private String fraudReason;
}
