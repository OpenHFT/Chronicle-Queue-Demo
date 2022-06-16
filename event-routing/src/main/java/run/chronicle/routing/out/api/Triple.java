package run.chronicle.routing.out.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class Triple extends SelfDescribingMarshallable {
    private long val;

    public long val() {
        return val;
    }

    public Triple val(long value) {
        this.val = value;
        return this;
    }
}
