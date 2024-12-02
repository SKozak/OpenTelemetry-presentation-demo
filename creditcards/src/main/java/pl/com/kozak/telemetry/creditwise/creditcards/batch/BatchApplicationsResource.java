package pl.com.kozak.telemetry.creditwise.creditcards.batch;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditCardApplicationRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batch-applications")
class BatchApplicationsResource {

    private final ApplicationBatchProcessing applicationBatchProcessing;

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createApplication(@RequestBody @Valid CreditCardApplicationRequest creditCardApplicationRequest) throws InterruptedException {
        applicationBatchProcessing.accept(creditCardApplicationRequest);
    }
}
