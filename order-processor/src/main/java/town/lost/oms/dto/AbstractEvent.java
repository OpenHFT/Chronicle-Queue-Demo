/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.AbstractMarshallable;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;

public class AbstractEvent<E extends AbstractEvent<E>> extends AbstractMarshallable {
    private String sender;

    private String target;

    // client sending time
    @LongConversion(MicroTimestampLongConverter.class)
    private long sendingTime;

    public String sender() {
        return sender;
    }

    public E sender(String sender) {
        this.sender = sender;
        return (E) this;
    }

    public String target() {
        return target;
    }

    public E target(String target) {
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
