package pl.com.kozak.telemetry.creditwise.creditcards.infrastructure.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
class ControllerAdviceExceptionHandler {

    @ExceptionHandler(Throwable.class)
    ResponseEntity<UnexpectedError> unexpectedErrorResponseEntity(Throwable throwable) {
        log.error("Unexpected error occurred ", throwable);
        return ResponseEntity.internalServerError().body(new UnexpectedError("Unexpected Error occured please try again latter"));
    }

}
