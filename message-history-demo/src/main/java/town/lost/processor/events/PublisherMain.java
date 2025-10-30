package town.lost.processor.events;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.time.UniqueMicroTimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;

public class PublisherMain {

    private static final int EVENTS = Integer.getInteger("events", 100_000);
    private static final int RATE = Integer.getInteger("rate", 10_000);

    public static void main(String[] args) {
        System.out.println("Started");
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder("in").sourceId(1).build()) {
            Events build = queue.methodWriterBuilder(Events.class).build();
            long start = System.nanoTime();
            long interval = (long) (1e9 / RATE);
            EventTwo two = new EventTwo();

            for (int i = 0; i < EVENTS; i++) {
                while (System.nanoTime() < start) {
                    Jvm.pause(1);
                }
                publish(build, two, "Hello World");
                start += interval;
            }
            publish(build, two, "Bye");
        }
        System.out.println("... Finished");
        System.exit(0);
    }

    private static void publish(Events build, EventTwo two, String text) {
        two.eventSource("publisher");
        two.eventTimeStamp(UniqueMicroTimeProvider.INSTANCE.currentTimeMicros());
        two.symbol(text);
        two.price(two.price + 1);
        two.quantiity(two.quantiity + 1);
        build.eventTwo(two);
    }
}
