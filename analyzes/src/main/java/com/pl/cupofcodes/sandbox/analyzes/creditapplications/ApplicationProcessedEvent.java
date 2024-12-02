package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import java.math.BigDecimal;

record ApplicationProcessedEvent(ApplicationState applicationState, String id, Long income, String number, DocumentType type,
                                 BigDecimal creditLimit, String pesel) {

}
