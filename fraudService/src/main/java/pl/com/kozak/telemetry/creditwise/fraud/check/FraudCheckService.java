package pl.com.kozak.telemetry.creditwise.fraud.check;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.Period;

@RequiredArgsConstructor
class FraudCheckService {
    private final FraudCheckRepository fraudCheckRepository;
    private final ExternalVerificationService externalVerificationService;

    public Mono<FraudResult> checkFraud(FraudCheck fraudCheck) {
        if (isUnderAge(fraudCheck.dateOfBirth())) {
            return Mono.just(FraudResult.underAge());
        }

        return checkAddressRisk(fraudCheck)
            .flatMap(addressRisk -> {
                if (addressRisk) {
                    return Mono.just(Boolean.FALSE);
                } else {
                    return externalVerificationService.verifyPersonalInformation(fraudCheck)
                        .flatMap(verificationResult -> {
                            if (verificationResult) {
                                return checkInternalFraudPatterns(fraudCheck);
                            } else {
                                return Mono.just(Boolean.FALSE);
                            }
                        });
                }
            })
            .map(aBoolean -> aBoolean ? FraudResult.fraud("not pass fraud rules") : FraudResult.ok());
    }

    private boolean isUnderAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears() < 18;
    }

    private Mono<Boolean> checkAddressRisk(FraudCheck fraudCheck) {
        return fraudCheckRepository.checkIfAddressIsInHighRisk(PotentialRiskyAddress.from(fraudCheck));
    }

    private Mono<Boolean> checkInternalFraudPatterns(FraudCheck fraudCheck) {
        return fraudCheckRepository.checkIfPreviouslyReported(fraudCheck);
    }
}
