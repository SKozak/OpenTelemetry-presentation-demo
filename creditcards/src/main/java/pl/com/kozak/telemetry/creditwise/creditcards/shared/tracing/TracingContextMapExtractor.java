package pl.com.kozak.telemetry.creditwise.creditcards.shared.tracing;

import io.opentelemetry.context.propagation.TextMapGetter;

import java.util.Map;

public class TracingContextMapExtractor implements TextMapGetter<Map<String, String>> {

    @Override
    public String get(Map<String, String> carrier, String key) {
        if (carrier.containsKey(key)) {
            return carrier.get(key);
        }
        return null;
    }

    @Override
    public Iterable<String> keys(Map<String, String> carrier) {
        return carrier.keySet();
    }
}
