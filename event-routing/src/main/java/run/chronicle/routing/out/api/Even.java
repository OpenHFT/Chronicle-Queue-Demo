package run.chronicle.routing.out.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class Even extends SelfDescribingMarshallable {
    private long val;

    public long val() {
        return val;
    }

    public Even val(long value) {
        this.val = value;
        return this;
    }
}
