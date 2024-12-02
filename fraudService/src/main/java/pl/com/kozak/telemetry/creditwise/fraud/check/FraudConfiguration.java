package pl.com.kozak.telemetry.creditwise.fraud.check;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
@RequiredArgsConstructor
class FraudConfiguration {
    private final ReactiveMongoOperations reactiveMongoOperations;
    @NotNull
    @Value("${fraudCheckServiceUrl:127.0.0.1}")
    private String fraudCheckServiceUrl;

    @Bean
    WebClient webClient() {
      log.info("Creating web client with url: {}", fraudCheckServiceUrl);
        return WebClient.builder()
            .baseUrl(fraudCheckServiceUrl)
            .build();
    }

    @Bean
    FraudCheckService fraudCheckService(FraudCheckRepository fraudCheckRepository, ExternalVerificationService externalVerificationService) {
        return new FraudCheckService(fraudCheckRepository, externalVerificationService);
    }

    @Bean
    FraudCheckRepository fraudCheckRepository() {
        return new FraudCheckRepository(reactiveMongoOperations);
    }

    @Bean
    ExternalVerificationService externalVerificationService(WebClient webClient) {
        return new ExternalVerificationService(webClient);
    }
}
