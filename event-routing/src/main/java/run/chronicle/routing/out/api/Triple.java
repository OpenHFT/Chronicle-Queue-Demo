package run.chronicle.routing.out.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class Triple extends SelfDescribingMarshallable {
    private long value;

    public long value() {
        return value;
    }

    public Triple value(long value) {
        this.value = value;
        return this;
    }
}
