package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

@Slf4j
@RequiredArgsConstructor
class JpaBasedApplicationRepository implements ApplicationRepository {

    private final JpaApplicationRepository applicationRepository;

    @Override
    @Transactional
    public CreditCardApplication create(CreditCardApplication creditCardApplication) {
        log.info("create new application {}", creditCardApplication);
        final CreditCardApplication savesavedApplication = applicationRepository.save(creditCardApplication);
        log.info("application with id  {} saved successfully", creditCardApplication.getId());
        return savesavedApplication;
    }


}

interface JpaApplicationRepository extends JpaRepository<CreditCardApplication, String> {

}
