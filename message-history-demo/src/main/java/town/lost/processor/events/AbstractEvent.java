package town.lost.processor.events;

import net.openhft.chronicle.wire.BytesInBinaryMarshallable;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;

public class AbstractEvent extends BytesInBinaryMarshallable {
    private String eventSource;

    @LongConversion(MicroTimestampLongConverter.class)
    private long eventTimeStamp;

    public String eventSource() {
        return eventSource;
    }

    public AbstractEvent eventSource(String eventSource) {
        this.eventSource = eventSource;
        return this;
    }

    public long eventTimeStamp() {
        return eventTimeStamp;
    }

    public AbstractEvent eventTimeStamp(long eventTimeStamp) {
        this.eventTimeStamp = eventTimeStamp;
        return this;
    }
}
