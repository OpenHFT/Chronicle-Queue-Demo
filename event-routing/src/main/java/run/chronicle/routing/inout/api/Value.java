package run.chronicle.routing.inout.api;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class Value extends SelfDescribingMarshallable {
    private long val;

    public Value(long val) {
        this.val = val;
    }

    public long val() {
        return val;
    }
}
