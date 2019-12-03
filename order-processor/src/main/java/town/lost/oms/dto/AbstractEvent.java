/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.AbstractBytesMarshallable;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;

public class AbstractEvent<E extends AbstractEvent<E>> extends AbstractBytesMarshallable {
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
}
