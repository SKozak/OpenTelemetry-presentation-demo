package com.pl.cupofcodes.sandbox.mockserver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

import static java.lang.Thread.sleep;

@Slf4j
@RestController
class MockController {

    private final Random random = new Random();

    @PostMapping("/external-fraud-check")
    ResponseEntity<Boolean> externalFraudCheck(@RequestBody FraudCheck fraudCheck) throws InterruptedException {
        log.info("start externalFraudCheck {}", fraudCheck);
        sleep(random.nextInt(100,800));
        final ResponseEntity<Boolean> response = random.nextInt(0, 10) > 5 ? ResponseEntity.ok(false) : ResponseEntity.ok(true);
        log.info("end externalFraudCheck {} with status {}", fraudCheck, response.getBody());
        return response;
    }
}
