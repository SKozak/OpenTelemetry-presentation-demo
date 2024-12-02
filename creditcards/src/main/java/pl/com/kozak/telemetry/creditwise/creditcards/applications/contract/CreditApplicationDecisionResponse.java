package pl.com.kozak.telemetry.creditwise.creditcards.applications.contract;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreditApplicationDecisionResponse {
    private String applicationNumber;
    private ApplicationStatus status;
    private BigDecimal creditLimit;
    private BigDecimal annualFee;
    private double interestRate;
    private String rewardsProgram;
    private double rewardsPercentage;
    private int rewardsLimit;
}
