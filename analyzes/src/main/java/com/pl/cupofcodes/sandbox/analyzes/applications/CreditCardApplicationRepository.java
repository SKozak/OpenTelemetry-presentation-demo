package com.pl.cupofcodes.sandbox.analyzes.applications;

import org.springframework.data.mongodb.repository.MongoRepository;

interface CreditCardApplicationRepository extends MongoRepository<CreditCardApplicationDocument, String>{
}
