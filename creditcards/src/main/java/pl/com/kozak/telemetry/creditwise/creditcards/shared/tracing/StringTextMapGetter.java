package pl.com.kozak.telemetry.creditwise.creditcards.shared.tracing;

import io.opentelemetry.context.propagation.TextMapGetter;

import static java.util.Collections.singletonList;

public class StringTextMapGetter implements TextMapGetter<String> {

    private static final StringTextMapGetter instance = new StringTextMapGetter();
    private static final String TRACEPARENT = "traceparent";

    public static StringTextMapGetter getInstance() {
        return instance;
    }

    @Override
    public Iterable<String> keys(String carrier) {
        return singletonList(TRACEPARENT);
    }

    @Override
    public String get(String carrier, String key) {
        if (TRACEPARENT.equals(key)) {
            return carrier;
        } else {
            return null;
        }
    }
}
