package pl.com.kozak.telemetry.creditwise.fraud.migration;

import lombok.experimental.UtilityClass;
import pl.com.kozak.telemetry.creditwise.fraud.check.FraudStatus;
import pl.com.kozak.telemetry.creditwise.fraud.check.PotentialRiskyAddress;
import pl.com.kozak.telemetry.creditwise.fraud.check.ReportedFraud;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@UtilityClass
public class DataGenerator {

    public static List<PotentialRiskyAddress> generateInitialAddresses() {
        return List.of(
            new PotentialRiskyAddress("Broniewskiego", "Zamość", "40-961"),
            new PotentialRiskyAddress("Orzeszkowej", "Lubartów", "75-782"),
            new PotentialRiskyAddress("Piłsudskiego", "Mielec", "30-996"),
            new PotentialRiskyAddress("Łabędzia", "Bełchatów", "71-804"),
            new PotentialRiskyAddress("Szymanowskiego", "Kętrzyn", "90-042"),
            new PotentialRiskyAddress("Świerczewskiego", "Kutno", "01-059"),
            new PotentialRiskyAddress("Andersa", "Biała Podlaska", "31-329"),
            new PotentialRiskyAddress("Cyprysowa", "Zamość", "17-038"),
            new PotentialRiskyAddress("Korfantego", "Dębica", "46-017"),
            new PotentialRiskyAddress("Partyzantów", "Iława", "63-983")
        );
    }

    public static List<ReportedFraud> generateInitialFrauds() {
        return List.of(
            new ReportedFraud(UUID.randomUUID().toString(), "Stefan", "jan71@ppuh.org", "Mućka", LocalDate.of(1950, 10, 23), "Promienna", "Sanok",
                "47-053", FraudStatus.FRAUD, "Tam wy kara ona."),
            new ReportedFraud(UUID.randomUUID().toString(), "Fabian", "inga63@yahoo.com", "Dębniak", LocalDate.of(1938, 3, 21), "Ciasna", "Koszalin"
                , "74-807", FraudStatus.OK, "Grać mistrz klub 1 przeciwny duch mrówka."),
            new ReportedFraud(UUID.randomUUID().toString(), "Adrianna", "drausgabriel@gmail.com", "Cisoń", LocalDate.of(1983, 2, 8), "Spokojna",
                "Chrzanów", "13-905", FraudStatus.OK, "Do ptak filmowy twój związać.")
        );
    }
}
