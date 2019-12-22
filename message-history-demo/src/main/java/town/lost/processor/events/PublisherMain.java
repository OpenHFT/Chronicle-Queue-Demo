package town.lost.processor.events;

import net.openhft.chronicle.core.time.UniqueMicroTimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;

public class PublisherMain {

    private static final int EVENTS = Integer.getInteger("events", 10);

    public static void main(String[] args) {
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder("in").sourceId(1).build()) {
            Events build = queue.methodWriterBuilder(Events.class).recordHistory(true).build();
            for (int i = 0; i < EVENTS; i++) {
                EventOne one = new EventOne();
                one.eventSource("publisher");
                one.eventTimeStamp(UniqueMicroTimeProvider.INSTANCE.currentTimeMicros());
                build.eventOne(one);
            }
        }
    }
}
