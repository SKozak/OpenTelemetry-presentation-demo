package pl.com.kozak.telemetry.creditwise.creditcards.fraud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "fraud-service")
interface ExternalSystemFraudCheckProvider {
    @PostMapping(value = "/fraud-check", consumes = APPLICATION_JSON_VALUE)
    FraudResult check(@RequestBody FraudCheck fraudCheck);
}
