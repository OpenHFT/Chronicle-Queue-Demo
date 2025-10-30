package town.lost.processor.events;

import java.util.concurrent.atomic.AtomicBoolean;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.io.IOTools;
import net.openhft.chronicle.queue.ChronicleQueue;

public class BridgeMain {
    static final AtomicBoolean RUNNING = new AtomicBoolean(true);

    public static void main(String[] args) {
        IOTools.deleteDirWithFiles("in", 2);
        IOTools.deleteDirWithFiles("out", 2);

        long events = 0, lastPrint = 0;
        try (ChronicleQueue queue = ChronicleQueue.singleBuilder("in").sourceId(1).build()) {
            try (ChronicleQueue queue2 = ChronicleQueue.singleBuilder("out").sourceId(2).build()) {

                Events out = queue2.methodWriterBuilder(Events.class).build();
                Events bridge = new BridgeEvents(out, RUNNING);
                MethodReader methodReader = queue.createTailer("bridge")
                        .methodReader(bridge);
                System.out.println("Started");
                long last = 0;
                while (RUNNING.get()) {
                    if (methodReader.readOne()) {
                        events++;
                    } else {
                        long now = System.currentTimeMillis();
                        if (lastPrint != events && now > last + 250) {
                            System.out.println("events: " + events);
                            lastPrint = events;
                            last = now;
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
    private final Events out;
    private final AtomicBoolean running;

    BridgeEvents(Events out, AtomicBoolean running) {
        this.out = out;
        this.running = running;
    }

    @Override
    public void eventOne(EventOne one) {
        out.eventOne(one);
        String text = one.text();
        if ("Bye".equals(text) || "bye".equals(text)) {
            running.set(false);
        }
    }

    @Override
    public void eventTwo(EventTwo two) {
        out.eventTwo(two);
    }
}
