package town.lost.processor.events;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.ChronicleQueue;

public class BridgeMain {
    public static void main(String[] args) {
        long events = 0, lastPrint = 0;
        try (ChronicleQueue queue = ChronicleQueue.single("in")) {
            try (ChronicleQueue queue2 = ChronicleQueue.singleBuilder("out").sourceId(2).build()) {

                Events out = queue2.methodWriterBuilder(Events.class).recordHistory(true).build();
                Events bridge = new BridgeEvents(out);
                MethodReader methodReader = queue.createTailer().methodReader(bridge);
                while (true) {
                    if (methodReader.readOne()) {
                        events++;
                    } else {
                        if (lastPrint != events) {
                            System.out.println("events: " + events);
                            lastPrint = events;
                        } else {
                            Thread.yield();
                        }
                    }
                }
            }
        }
    }
}

class BridgeEvents implements Events {
    final Events out;

    public BridgeEvents(Events out) {
        this.out = out;
    }

    @Override
    public void eventOne(EventOne one) {
        out.eventOne(one);
    }

    @Override
    public void eventTwo(EventTwo two) {
        out.eventTwo(two);
    }
}
