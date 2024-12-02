package com.pl.cupofcodes.sandbox.analyzes.applications;

import java.math.BigDecimal;

record ApplicationProcessedEvent(String applicationState, String id, Long income, String number, String type,
                                 BigDecimal creditLimit, String destination, String pesel){

}
