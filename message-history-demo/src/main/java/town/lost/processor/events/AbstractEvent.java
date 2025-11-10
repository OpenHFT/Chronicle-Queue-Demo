//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package town.lost.processor.events;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.NanoTime;

@SuppressWarnings("unchecked")
public class AbstractEvent<E extends AbstractEvent<E>> extends SelfDescribingMarshallable {
    private String eventSource;

    @NanoTime
    private long eventTimeStamp;

    public String eventSource() {
        return eventSource;
    }

    public E eventSource(String eventSource) {
        this.eventSource = eventSource;
        return (E) this;
    }

    public long eventTimeStamp() {
        return eventTimeStamp;
    }

    public E eventTimeStamp(long eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
        return (E) this;
    }
}
