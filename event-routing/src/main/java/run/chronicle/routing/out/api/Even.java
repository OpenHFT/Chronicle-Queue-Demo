package run.chronicle.routing.out.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class Even extends SelfDescribingMarshallable {
    private long value;

    public long value() {
        return value;
    }

    public Even value(long value) {
        this.value = value;
        return this;
    }
}
