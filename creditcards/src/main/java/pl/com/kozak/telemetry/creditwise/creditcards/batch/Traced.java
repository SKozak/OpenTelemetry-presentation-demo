package pl.com.kozak.telemetry.creditwise.creditcards.batch;

import lombok.NonNull;
import lombok.Value;

import java.util.Map;

@Value
class Traced<T> {
    @NonNull
    private final T value;
    @NonNull
    private final Map<String, String> traceContext;
}
