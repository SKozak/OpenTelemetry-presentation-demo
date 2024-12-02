package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Context;

import java.util.Map;

public interface TracingProvider {
    Map<String,String> getContext();
    String traceIdStringFromContext(Context context);
    Span startSpan(String name, Context parent, String linkedSpanId);
    Context contextFromTraceIdString(String traceId);
    String getCurrentTraceParentAsString();
//    Map<String, String> createLinkedTrace(List<Map<String, String>> tracingContexts);
//    SpanBuilder createLinkedSpan(List<Map<String, String>> tracingContexts);
//    SpanBuilder createLinkedSpan(String spanName, Context currentContext, Map<String, String> parentContext);
}
