package pl.com.kozak.telemetry.creditwise.fraud.check;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
class FraudResource {
    private final FraudCheckService fraudCheckService;

    @PostMapping("/fraud-check")
    Mono<FraudResult> checkFraud(@RequestBody FraudCheck fraudCheck) {
        return fraudCheckService.checkFraud(fraudCheck);
    }
}
