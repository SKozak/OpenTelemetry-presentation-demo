package pl.com.kozak.telemetry.creditwise.fraud.check;


import static pl.com.kozak.telemetry.creditwise.fraud.check.FraudStatus.*;

public record FraudResult(FraudStatus fraudStatus, String rejectionReason) {
    public static FraudResult underAge() {
        return new FraudResult(REJECTED, "Under age");
    }

    public static FraudResult fraud(String additionalMessage) {
        return new FraudResult(FRAUD, additionalMessage);
    }

    public static FraudResult ok() {
        return new FraudResult(OK, null);
    }
}
