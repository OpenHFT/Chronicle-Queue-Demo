package run.chronicle.routing.inout.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class Value extends SelfDescribingMarshallable {
    private long value;

    public Value(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }
}
