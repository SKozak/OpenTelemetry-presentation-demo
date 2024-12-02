package pl.com.kozak.telemetry.creditwise.creditcards.identity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class IdentityRegistryFacade {

    private final IdentityRegistryClient identityRegistryClient;

    public IdentificationResult identifyClientDocument(DocumentToVerification document) {
        log.info("trying to identify client documet {}", document);
        final IdentificationResult identificationResult = identityRegistryClient.verifyDocument(document);
        log.info("client document verified with response {}", identificationResult);
        return identificationResult;
    }
}
