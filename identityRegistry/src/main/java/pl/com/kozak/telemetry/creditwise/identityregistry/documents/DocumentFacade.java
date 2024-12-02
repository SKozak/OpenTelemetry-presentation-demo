package pl.com.kozak.telemetry.creditwise.identityregistry.documents;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Random;

import static java.lang.Thread.sleep;
import static pl.com.kozak.telemetry.creditwise.identityregistry.documents.IdentificationResult.OK;
import static pl.com.kozak.telemetry.creditwise.identityregistry.documents.IdentificationResult.STOLEN;

@Slf4j
@RequiredArgsConstructor
class DocumentFacade {
//    private final DocumentRepository documentRepository;

//    private final DocumentVerificationAttempReposiotory documentVerificationAttempReposiotory;

    public IdentificationResult verifyDocument(DocumentToVerification document) {
        Random rnd = new Random();
        log.info("start  verifyDocument" + document);
        try {
            sleep(rnd.nextInt(100, 500));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (rnd.nextInt(0,10) > 5) {
            log.info("end  verifyDocument {} with status {}", document.getNumber(), STOLEN);
            throw new DocumentVerificationException(document.getNumber(), STOLEN);
        }
        log.info("end  verifyDocument {} with status {}", document.getNumber(), OK);
        return OK;
    }
}
