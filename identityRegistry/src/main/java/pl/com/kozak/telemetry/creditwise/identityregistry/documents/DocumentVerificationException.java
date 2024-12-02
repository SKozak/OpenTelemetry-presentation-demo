package pl.com.kozak.telemetry.creditwise.identityregistry.documents;

class DocumentVerificationException extends RuntimeException {
    public DocumentVerificationException(String number, IdentificationResult identificationResult) {
        super("Document " + number + " verification failed with status " + identificationResult);
    }
}
