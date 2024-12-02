package pl.com.kozak.telemetry.creditwise.creditcards.fraud;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
class FraudConfiguration {

    private final ExternalSystemFraudCheckProvider externalSystemFraudCheckProvider;

    @Bean
    FraudFacade fraudFacade(FraudCheckProvider fraudCheckProvider) {
        return new FraudFacade(fraudCheckProvider);
    }

    @Bean
    FraudCheckProvider fraudCheckProvider() {
        return new FraudCheckProvider(externalSystemFraudCheckProvider);
    }
}
