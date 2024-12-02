package pl.com.kozak.telemetry.creditwise.fraud.check;

import lombok.Value;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Value
@Document
public class PotentialRiskyAddress {
    @Indexed
    String street;
    @Indexed
    String city;
    @Indexed
    String zip;

    public PotentialRiskyAddress(String street, String city, String zip) {
        this.street = street;
        this.city = city;
        this.zip = zip;
    }

    public static PotentialRiskyAddress from(FraudCheck fraudCheck) {
        return new PotentialRiskyAddress(fraudCheck.street(), fraudCheck.city(), fraudCheck.zip());
    }
}
