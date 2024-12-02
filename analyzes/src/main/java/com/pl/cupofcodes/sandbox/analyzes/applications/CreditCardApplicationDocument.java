package com.pl.cupofcodes.sandbox.analyzes.applications;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@Document
@AllArgsConstructor
public class CreditCardApplicationDocument {
    @Id
    private String id;
    private BigDecimal limit;

}
