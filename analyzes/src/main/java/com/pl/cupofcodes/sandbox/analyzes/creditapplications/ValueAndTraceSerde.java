package com.pl.cupofcodes.sandbox.analyzes.creditapplications;

import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serializer;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

public class ValueAndTraceSerde<V> implements Serde<ValueAndTrace<V>> {

    private final Serde<V> inner;

    public ValueAndTraceSerde(Serde<V> inner) {
        this.inner = inner;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        Serde.super.configure(configs, isKey);
        inner.configure(configs, isKey);
    }

    @Override
    public Serializer<ValueAndTrace<V>> serializer() {
        return (topic, data) -> {
            if (data.getValue() == null) {
                return null;
            }
            byte[] payload = inner.serializer().serialize(topic, data.getValue());
            byte[] traceparent = data.getParentContext().getBytes(StandardCharsets.UTF_8);
            byte[] combined = Arrays.copyOf(traceparent, traceparent.length + payload.length);
            System.arraycopy(payload, 0, combined, traceparent.length, payload.length);
            return combined;
        };
    }

    @Override
    public Deserializer<ValueAndTrace<V>> deserializer() {
        return (topic, data) -> {
            if (data == null) {
                return null;
            }
            if (data.length < 56) {
                return new ValueAndTrace<>(null, inner.deserializer().deserialize(topic, data));
            }
            byte[] traceparent = new byte[55];
            System.arraycopy(data, 0, traceparent, 0, 55);
            byte[] value = new byte[data.length - 55];
            System.arraycopy(data, 55, value, 0, data.length - 55);
            return new ValueAndTrace<>(new String(traceparent, StandardCharsets.UTF_8),
                inner.deserializer()
                    .deserialize(topic, value));
        };
    }
}
