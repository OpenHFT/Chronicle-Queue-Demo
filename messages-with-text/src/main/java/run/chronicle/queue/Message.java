package run.chronicle.queue;

import net.openhft.chronicle.bytes.Bytes;
import net.openhft.chronicle.wire.BytesInBinaryMarshallable;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MilliTimestampLongConverter;

public class Message extends BytesInBinaryMarshallable {
    private final Bytes<?> text = Bytes.allocateElasticOnHeap();

    @LongConversion(MilliTimestampLongConverter.class)
    private long timeStamp;

    //Getters and Setters
    public Bytes<?> getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text.clear().append(text);
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}