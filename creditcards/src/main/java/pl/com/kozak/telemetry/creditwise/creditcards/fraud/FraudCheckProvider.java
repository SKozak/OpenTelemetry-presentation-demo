package pl.com.kozak.telemetry.creditwise.creditcards.fraud;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class FraudCheckProvider {
    private final ExternalSystemFraudCheckProvider externalSystemFraudCheckProvider;

    FraudResult check(FraudCheck fraudCheck) {
        return externalSystemFraudCheckProvider.check(fraudCheck);
    }
}
