/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.*;

public class AbstractEvent<E extends AbstractEvent<E>> extends AbstractMarshallable {
    @LongConversion(Base85LongConverter.class)
    private long sender;

    @LongConversion(Base85LongConverter.class)
    private long target;

    // client sending time
    @LongConversion(MicroTimestampLongConverter.class)
    private long sendingTime;

    public long sender() {
        return sender;
    }

    public E sender(long sender) {
        this.sender = sender;
        return (E) this;
    }

    public long target() {
        return target;
    }

    public E target(long target) {
        this.target = target;
        return (E) this;
    }

    public long sendingTime() {
        return sendingTime;
    }

    public E sendingTime(long sendingTime) {
        this.sendingTime = sendingTime;
        return (E) this;
    }

    public void writeMarshallable0(WireOut out) {
        out.write("sender").writeLong(sender);
        out.write("target").writeLong(target);
        out.write("sendingTime").writeLong(sendingTime);
    }

    public void readMarshallable0(WireIn in) {
        sender = in.read("sender").readLong();
        target = in.read("target").readLong();
        sendingTime = in.read("sendingTime").readLong();
    }
}
