package pl.com.kozak.telemetry.creditwise.creditcards.applications;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditCardApplicationRequest;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
interface CreditCardApplicationMapper {
    @Mapping(target = "applicationState", constant = "NEW")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "applicationStateDescription", constant = "New")
    CreditCardApplication createNewFrom(CreditCardApplicationRequest request, Double annualFee, Double creditLimit);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dateOfBirth", source = "birthDate")
    @Mapping(target = "address.zip", source = "address.postalCode")
    @Mapping(target = "employment.employmentStartDate", source = "employment.startDate")
    Client createNewFrom(CreditCardApplicationRequest.Client client);
}
