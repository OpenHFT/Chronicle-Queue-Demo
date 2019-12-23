package town.lost.processor.events;

import net.openhft.chronicle.core.time.UniqueMicroTimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;

public class PublisherMain {

    private static final int EVENTS = Integer.getInteger("events", 100);

    public static void main(String[] args) {
        System.out.println("Started");
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder("in").sourceId(1).build()) {
            Events build = queue.methodWriterBuilder(Events.class).recordHistory(true).build();
            for (int i = 0; i < EVENTS; i++) {
                publish(build, "Hello World");
                Thread.yield();
            }
            publish(build, "Bye");
        }
        System.out.println("... Finished");
        System.exit(0);
    }

    private static void publish(Events build, String text) {
        EventOne one = new EventOne();
        one.eventSource("publisher");
        one.eventTimeStamp(UniqueMicroTimeProvider.INSTANCE.currentTimeMicros());
        one.text(text);
        build.eventOne(one);
    }
}
