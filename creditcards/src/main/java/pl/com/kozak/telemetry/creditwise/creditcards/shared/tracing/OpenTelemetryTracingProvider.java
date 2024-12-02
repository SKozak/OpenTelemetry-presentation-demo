package pl.com.kozak.telemetry.creditwise.creditcards.shared.tracing;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanBuilder;
import io.opentelemetry.api.trace.SpanContext;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Context;
import io.opentelemetry.context.propagation.TextMapPropagator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Qualifier("kstreamTracingProvider")
class OpenTelemetryTracingProvider implements TracingProvider {

    private static final String TRACEPARENT = "traceparent";
    private final OpenTelemetry openTelemetry;
    private final Tracer tracer;

    OpenTelemetryTracingProvider(OpenTelemetry openTelemetry) {
        this.openTelemetry = openTelemetry;
        this.tracer = openTelemetry.getTracer("io.opentelemetry.kafka-streams-0.11");
    }

    @Override
    public Map<String, String> getContext() {
        final Map<String, String> map = new HashMap<>();
        final TextMapPropagator textMapPropagator = openTelemetry.getPropagators().getTextMapPropagator();
        textMapPropagator.inject(Context.current(), map, Map::put);
        return map;
    }

    @Override
    public String traceIdStringFromContext(Context context) {
        String[] traceIdentifierHolder = new String[1];
        openTelemetry.getPropagators().getTextMapPropagator()
            .inject(context, traceIdentifierHolder, (carrier, key, value) -> {
                if (TRACEPARENT.equals(key)) {
                    carrier[0] = value;
                }
            });
        return traceIdentifierHolder[0];
    }

    @Override
    public Span startSpan(String name, Context parent, String traceParentToLink) {
        SpanBuilder spanBuilder = tracer.spanBuilder(name).setParent(parent);
        if (traceParentToLink != null) {
            SpanContext linkedSpanContext = Span.fromContext(contextFromTraceIdString(traceParentToLink))
                .getSpanContext();
            spanBuilder.addLink(linkedSpanContext);
        }
        return spanBuilder.startSpan();
    }


    @Override
    public Context contextFromTraceIdString(String traceId) {
        return openTelemetry.getPropagators().getTextMapPropagator()
            .extract(Context.current(), traceId, StringTextMapGetter.getInstance());
    }

    @Override
    public String getCurrentTraceParentAsString() {
        return getContext().get(TRACEPARENT);
    }
}
