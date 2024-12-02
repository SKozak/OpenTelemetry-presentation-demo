package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CreditApplicationDeclined {

    private final String declineReason;

    public static CreditApplicationDeclined of(String declineReason) {
        return new CreditApplicationDeclined(declineReason);
    }
}
