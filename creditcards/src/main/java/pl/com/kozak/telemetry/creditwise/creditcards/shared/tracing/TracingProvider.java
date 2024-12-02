package pl.com.kozak.telemetry.creditwise.creditcards.shared.tracing;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;

import java.util.Map;

public interface TracingProvider {
    Map<String,String> getContext();
    String traceIdStringFromContext(Context context);
    Span startSpan(String name, Context parent, String linkedSpanId);
    Context contextFromTraceIdString(String traceId);
    String getCurrentTraceParentAsString();
}
