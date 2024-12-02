package com.pl.cupofcodes.sandbox.analyzes.creditapplications.sink;

import org.springframework.data.mongodb.repository.MongoRepository;

interface CreditLimitSummaryRepository extends MongoRepository<CreditLimitSummaryDocument, String>{
}
