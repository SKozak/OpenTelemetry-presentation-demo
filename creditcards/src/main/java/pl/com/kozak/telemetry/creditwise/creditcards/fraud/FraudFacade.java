package pl.com.kozak.telemetry.creditwise.creditcards.fraud;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FraudFacade {
    private final FraudCheckProvider fraudCheckProvider;
    public FraudResult analyze(FraudCheck fraudCheck) {
        return fraudCheckProvider.check(fraudCheck);
    }
}
