package town.lost.processor.events;

import net.openhft.chronicle.bytes.HexDumpBytes;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.wire.*;

public class DumpOutMain {
    public static void main(String[] args) {
//        DumpQueueMain.dump("out");
        System.out.println("Started");
        try (ChronicleQueue queue2 = ChronicleQueue.singleBuilder("out").sourceId(2).build()) {
            // raw read so you don't need to know the message types.
            ExcerptTailer tailer = queue2.createTailer();
            StringBuilder key = new StringBuilder();
            StringBuilder key2 = new StringBuilder();
            VanillaMessageHistory mh = new VanillaMessageHistory();
            mh.addSourceDetails(true);
            DummyAbstractEvent dae = new DummyAbstractEvent();
            boolean first = true;
            long last = Long.MAX_VALUE / 2;
            while (true) {
                try (DocumentContext dc = tailer.readingDocument()) {
                    if (!dc.isPresent()) {
                        if (System.currentTimeMillis() < last + 2500)
                            continue;
                        break;
                    }
                    last = System.currentTimeMillis();
                    Wire wire = dc.wire();
                    wire.read(key).object(mh, VanillaMessageHistory.class);

                    // read the event number
                    wire.read(key2).object(dae, DummyAbstractEvent.class);

                    System.out.println(mh + " - " + key2 + ", source: " + dae.eventSource() + ", ts: " + MicroTimestampLongConverter.INSTANCE.asString(dae.eventTimeStamp()));
                    if (first) {
                        first = false;
                        Wire wire2 = new BinaryWire(new HexDumpBytes());
                        EventWithHistory ewh = wire2.methodWriter(EventWithHistory.class);
                        ewh.history(mh)
                                .eventOne(new EventOne()
                                        .eventSource(dae.eventSource())
                                        .eventTimeStamp(dae.eventTimeStamp()));
                        System.out.println(wire2.bytes().toHexString());
                    }
                }
            }
        }
        System.out.println(".. Finished");
        System.exit(0);
    }

}

class DummyAbstractEvent extends AbstractEvent {

}