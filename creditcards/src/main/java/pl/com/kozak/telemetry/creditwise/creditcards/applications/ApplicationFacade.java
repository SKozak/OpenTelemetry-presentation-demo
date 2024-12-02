package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditApplicationDecisionResponse;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditCardApplicationRequest;
import pl.com.kozak.telemetry.creditwise.creditcards.fraud.FraudCheck;
import pl.com.kozak.telemetry.creditwise.creditcards.fraud.FraudFacade;
import pl.com.kozak.telemetry.creditwise.creditcards.fraud.FraudResult;
import pl.com.kozak.telemetry.creditwise.creditcards.identity.DocumentToVerification;
import pl.com.kozak.telemetry.creditwise.creditcards.identity.IdentificationResult;
import pl.com.kozak.telemetry.creditwise.creditcards.identity.IdentityRegistryFacade;
import pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.outbox.Outbox;

import static pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.ApplicationStatus.APPROVED;
import static pl.com.kozak.telemetry.creditwise.creditcards.identity.IdentificationResult.OK;

@Slf4j
@RequiredArgsConstructor
class ApplicationFacade {

    private static final double DEFAULT_ANNUAL_FEE = 0.15;
    private static final double DEFAULT_CREDIT_LIMIT = 50000.0;
    private final ApplicationRepository applicationRepository;
    private final IdentityRegistryFacade identityRegistryFacade;
    private final CreditCardApplicationMapper creditCardApplicationMapper;
    private final FraudFacade fraudFacade;
    private final Outbox outbox;
    private final String destination;

    @Transactional
    public Either<CreditApplicationDeclined, CreditApplicationDecisionResponse> process(CreditCardApplicationRequest creditCardApplicationRequest) {
        final CreditCardApplication savedApplication = applicationRepository.create(mapFromRequest(creditCardApplicationRequest));
        final Document document = savedApplication.getDocument();
        final IdentificationResult identificationResult = identityRegistryFacade.identifyClientDocument(DocumentToVerification.builder()
            .expiry(document.getExpiry())
            .number(document.getNumber())
            .type(document.getType())
            .build()
        );
        if (OK != identificationResult) {
            log.warn("client document verification not passes, decline application");
            savedApplication.decline(identificationResult);
            log.warn("application {} was declined with reason {}", savedApplication.getId(), savedApplication.getDeclineReason());
            outbox.save(ApplicationProcessedEvent.from(savedApplication, destination));
            return Either.left(CreditApplicationDeclined.of(savedApplication.getDeclineReason()));
        }

        final FraudResult fraudAnalyzeResult = fraudFacade.analyze(toFraudCheck(savedApplication));

        if (fraudAnalyzeResult.isfraud()) {
            log.warn("fraud check not passes, decline application");
            savedApplication.decline(fraudAnalyzeResult);
            log.warn("application {} was declined with reason {}", savedApplication.getId(), savedApplication.getDeclineReason());
            outbox.save(ApplicationProcessedEvent.from(savedApplication, destination));
            return Either.left(CreditApplicationDeclined.of(savedApplication.getDeclineReason()));
        }

        savedApplication.approve();
        outbox.save(ApplicationProcessedEvent.from(savedApplication, destination));
        return Either.right(prepareResponse(savedApplication, identificationResult, fraudAnalyzeResult));
    }

    private FraudCheck toFraudCheck(CreditCardApplication savedApplication) {
        return new FraudCheck(
            savedApplication.getClientFirstName(),
            savedApplication.getClientEmail(),
            savedApplication.getClientLastName(),
            savedApplication.getClientBirthDate(),
            savedApplication.getClientStreet(),
            savedApplication.getClientCity(),
            savedApplication.getClientZipCode()
        );
    }

    private CreditApplicationDecisionResponse prepareResponse(CreditCardApplication savedApplication, IdentificationResult identificationResult,
                                                              FraudResult analyzeResult) {
        final CreditApplicationDecisionResponse decisionResponse = new CreditApplicationDecisionResponse();
        decisionResponse.setStatus(APPROVED);
        return decisionResponse;
    }

    private CreditCardApplication mapFromRequest(CreditCardApplicationRequest request) {
        return creditCardApplicationMapper.createNewFrom(request, DEFAULT_ANNUAL_FEE, DEFAULT_CREDIT_LIMIT);
    }
}
