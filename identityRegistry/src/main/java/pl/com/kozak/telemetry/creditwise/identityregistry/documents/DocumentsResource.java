package pl.com.kozak.telemetry.creditwise.identityregistry.documents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequiredArgsConstructor
class DocumentsResource {
    private final DocumentFacade documentFacade;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    IdentificationResult verifyDocument(@RequestBody DocumentToVerification document) {
        return documentFacade.verifyDocument(document);
    }

    @ExceptionHandler(DocumentVerificationException.class)
    ResponseEntity<String> handleDocumentVerificationException(DocumentVerificationException e) {
        log.error("Document verification failed", e);
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
