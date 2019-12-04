/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.wire.*;

public class AbstractEvent<E extends AbstractEvent<E>> extends BytesInBinaryMarshallable {
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

    private static final int MASHALLABLE_VERSION = 1;

    @Override
    public void writeMarshallable(BytesOut out) {
        out.writeStopBit(MASHALLABLE_VERSION);
        out.writeLong(sender);
        out.writeLong(target);
        out.writeLong(sendingTime);
    }

    @Override
    public void readMarshallable(BytesIn in) {
        int version = (int) in.readStopBit();
        if (version == MASHALLABLE_VERSION) {
            super.readMarshallable(in);
            sender = in.readLong();
            target = in.readLong();
            sendingTime = in.readLong();
        } else {
            throw new IllegalStateException("Unknown version " + version);
        }
    }

    @Override
    public void writeMarshallable(WireOut out) {
        if (out.getValueOut().isBinary())
            throw new AssertionError();
        out.write("sender").writeLong(Base85LongConverter.INSTANCE, sender);
        out.write("target").writeLong(Base85LongConverter.INSTANCE, target);
        out.write("sendingTime").writeLong(MicroTimestampLongConverter.INSTANCE, sendingTime);
    }

    @Override
    public void readMarshallable(WireIn in) {
        sender = in.read("sender").readLong(Base85LongConverter.INSTANCE);
        target = in.read("target").readLong(Base85LongConverter.INSTANCE);
        sendingTime = in.read("sendingTime").readLong(MicroTimestampLongConverter.INSTANCE);
    }
}
