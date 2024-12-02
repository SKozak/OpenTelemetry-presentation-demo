package pl.com.kozak.telemetry.creditwise.creditcards.batch;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.*;
import io.opentelemetry.context.Context;
import io.opentelemetry.instrumentation.annotations.WithSpan;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import pl.com.kozak.telemetry.creditwise.creditcards.applications.contract.CreditCardApplicationRequest;
import pl.com.kozak.telemetry.creditwise.creditcards.shared.tracing.TracingContextMapExtractor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;

@Slf4j
@RequiredArgsConstructor
class ApplicationBatchProcessing {
    private final Queue<Traced<CreditCardApplicationRequest>> queue = new ConcurrentLinkedDeque<>();
    private final BatchPublisher batchPublisher;
    private final Tracer tracer;
    private final OpenTelemetry openTelemetry;


    @Scheduled(fixedRate = 3000)
    void processBatch() {
        log.info("Start batch processing");
        int processedCount = 0;
        BigDecimal totalCreditLimit = BigDecimal.ZERO;
        List<SpanContext> tracingContexts = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            Traced<CreditCardApplicationRequest> tracedElement = queue.poll();

            if (tracedElement == null) {
                break;
            }

            final Context contextToLink = openTelemetry.getPropagators()
                    .getTextMapPropagator()
                    .extract(Context.current(), tracedElement.getTraceContext(), new TracingContextMapExtractor());

            tracingContexts.add(Span.fromContext(contextToLink).getSpanContext());
            CreditCardApplicationRequest element = tracedElement.getValue();

            totalCreditLimit = totalCreditLimit.add(element.getRequestedCardLimit());
            processedCount++;
        }

        SpanBuilder spanBuilder = tracer.spanBuilder("batch-processing")
                .setSpanKind(SpanKind.INTERNAL);
        tracingContexts.forEach(spanBuilder::addLink);
        final Span serverSpan = spanBuilder.startSpan();

        try  {
            if (processedCount > 0) {
                BigDecimal averageCreditLimit = totalCreditLimit.divide(BigDecimal.valueOf(processedCount), RoundingMode.HALF_UP);
                final BatchResult batchResult = new BatchResult(UUID.randomUUID().toString(), averageCreditLimit, processedCount);
                batchPublisher.publish(batchResult);
            }
        } finally {
            serverSpan.end();
        }

        log.info("End batch processing");
    }

    @WithSpan("added-to-batch")
    public void accept(CreditCardApplicationRequest creditCardApplicationRequest) throws InterruptedException {
        Map<String,String> traceContext = new HashMap<>();
        openTelemetry.getPropagators().getTextMapPropagator().inject(Context.current(), traceContext, Map::put);
        log.info("Accepting application");
        Thread.sleep(20);
        queue.add(new Traced<>(creditCardApplicationRequest, traceContext));
    }
}
