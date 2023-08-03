package town.lost.processor.events;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.wire.MessageHistory;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;

public class DumpDecodedMain {
    public static void main(String[] args) {
        try (ChronicleQueue queue2 = ChronicleQueue.singleBuilder("out").sourceId(2).build()) {
            long last = Long.MAX_VALUE / 2;
            MethodReader methodReader = queue2.createTailer("dump")
                    .methodReader(new EventHandler());
            System.out.println("Started");
            while (true) {
                long now = System.currentTimeMillis();
                if (methodReader.readOne()) {
                    last = now;
                } else {
                    // stop if nothing for 2.5 seconds.
                    if (now > last + 2500)
                        break;
                    Thread.yield();
                }
            }
        }
        System.out.println(".. Finished");
        System.exit(0);
    }

    private static class EventHandler implements Events {
        @Override
        public void eventOne(EventOne one) {
            printMessageHistory("eventOne", one);
            // use EventOne here
        }

        @Override
        public void eventTwo(EventTwo two) {
            printMessageHistory("eventTwo", two);
            // use EventTwo here
        }

        private void printMessageHistory(String eventName, AbstractEvent<?> event) {
            MessageHistory mh = MessageHistory.get();
            System.out.println(mh + " - " + eventName + ", source: " + event.eventSource() + ", ts: " + MicroTimestampLongConverter.INSTANCE.asString(event.eventTimeStamp()));
            System.out.println(event);
        }
    }
}
