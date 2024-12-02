package pl.com.kozak.telemetry.creditwise.fraud.check;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
class ExternalVerificationService {
    private final WebClient webClient;

    Mono<Boolean> verifyPersonalInformation(FraudCheck fraudCheck) {
        return webClient.post()
            .uri("/external-fraud-check")
            .bodyValue(fraudCheck)
            .exchangeToMono(clientResponse -> clientResponse.bodyToMono(Boolean.class));
    }
}
