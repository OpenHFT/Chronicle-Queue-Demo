package town.lost.processor.events;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.ChronicleQueue;

public class BridgeMain {
    static boolean running = true;

    public static void main(String[] args) {
        long events = 0, lastPrint = 0;
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder("in").sourceId(1).build()) {
            try (ChronicleQueue queue2 = ChronicleQueue.singleBuilder("out").sourceId(2).build()) {

                Events out = queue2.methodWriterBuilder(Events.class).recordHistory(true).build();
                Events bridge = new BridgeEvents(out);
                MethodReader methodReader = queue.createTailer().methodReader(bridge);
                System.out.println("Started");
                while (running) {
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
        System.out.println("... finished");
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
        if (one.text().equalsIgnoreCase("Bye"))
            BridgeMain.running = false;
    }

    @Override
    public void eventTwo(EventTwo two) {
        out.eventTwo(two);
    }
}
