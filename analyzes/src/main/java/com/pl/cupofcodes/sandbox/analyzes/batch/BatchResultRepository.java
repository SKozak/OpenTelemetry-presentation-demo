package com.pl.cupofcodes.sandbox.analyzes.batch;

import org.springframework.data.mongodb.repository.MongoRepository;

interface BatchResultRepository extends MongoRepository<BatchResultDocument, String>{
}
