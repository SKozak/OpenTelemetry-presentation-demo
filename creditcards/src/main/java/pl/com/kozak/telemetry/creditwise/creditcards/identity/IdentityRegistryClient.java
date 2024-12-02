package pl.com.kozak.telemetry.creditwise.creditcards.identity;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@FeignClient(name = "identity-registry-service")
interface IdentityRegistryClient {
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    IdentificationResult verifyDocument(@RequestBody DocumentToVerification document);

}

