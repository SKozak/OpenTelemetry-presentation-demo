package pl.com.kozak.telemetry.creditwise.creditcards.fraud;


public record FraudResult(FraudStatus fraudStatus, String rejectionReason) {
    public boolean isfraud() {
        return fraudStatus == FraudStatus.FRAUD;
    }
}
