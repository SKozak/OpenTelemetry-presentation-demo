package pl.com.kozak.telemetry.creditwise.creditcards.identity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class IdentityRegistryConfiguration {

    @Bean
    IdentityRegistryFacade identityRegistryFacade(IdentityRegistryClient identityRegistryClient) {
        return new IdentityRegistryFacade(identityRegistryClient);
    }
}
