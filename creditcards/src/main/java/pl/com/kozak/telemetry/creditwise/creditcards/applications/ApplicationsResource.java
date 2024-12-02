package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.context.Context;
import io.vavr.control.Either;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditApplicationDecisionResponse;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditCardApplicationRequest;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/applications")
class ApplicationsResource {

    private final ApplicationFacade applicationFacade;
    private final ApplicationFacade applicationFacadeWithDebezium;

    @PostMapping
    public ResponseEntity<?> createApplication(@RequestBody @Valid CreditCardApplicationRequest creditCardApplicationRequest) {
        try (var baggageScope = Baggage.builder()
                .put("ClientId", UUID.randomUUID().toString())
                .build()
                .storeInContext(Context.current()).makeCurrent()) {
            final Either<CreditApplicationDeclined, CreditApplicationDecisionResponse> creditApplicationDecisionResponse = applicationFacade.process(creditCardApplicationRequest);
            return creditApplicationDecisionResponse.fold(
                    creditApplicationDeclined -> ResponseEntity.badRequest().body(creditApplicationDeclined),
                    ResponseEntity::ok
            );
        }
    }

    @PostMapping("/debezium")
    public ResponseEntity<?> createApplication2(@RequestBody @Valid CreditCardApplicationRequest creditCardApplicationRequest) {
        final Either<CreditApplicationDeclined, CreditApplicationDecisionResponse> creditApplicationDecisionResponse = applicationFacadeWithDebezium.process(creditCardApplicationRequest);
        return creditApplicationDecisionResponse.fold(
                creditApplicationDeclined -> ResponseEntity.badRequest().body(creditApplicationDeclined),
                ResponseEntity::ok
        );
    }
}
