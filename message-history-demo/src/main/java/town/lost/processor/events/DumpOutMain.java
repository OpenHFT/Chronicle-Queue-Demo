package town.lost.processor.events;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.wire.DocumentContext;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;
import net.openhft.chronicle.wire.VanillaMessageHistory;
import net.openhft.chronicle.wire.Wire;

import java.io.FileNotFoundException;

public class DumpOutMain {
    public static void main(String[] args) throws FileNotFoundException {
//        DumpQueueMain.dump("out");
        try (ChronicleQueue queue2 = ChronicleQueue.single("out")) {
            // raw read so you don't need to know the message types.
            ExcerptTailer tailer = queue2.createTailer();
            StringBuilder key = new StringBuilder();
            StringBuilder key2 = new StringBuilder();
            VanillaMessageHistory mh = new VanillaMessageHistory();
            DummyAbstractEvent dae = new DummyAbstractEvent();
            while (true) {
                try (DocumentContext dc = tailer.readingDocument()) {
                    if (!dc.isPresent())
                        break;
                    Wire wire = dc.wire();
                    wire.read(key).object(mh, VanillaMessageHistory.class);

                    // read the event number
                    wire.read(key2).object(dae, DummyAbstractEvent.class);

                    System.out.println(mh + " - " + key2 + ", source: " + dae.eventSource() + ", ts: " + MicroTimestampLongConverter.INSTANCE.asString(dae.eventTimeStamp()));
                }
            }
        }
    }
}

class DummyAbstractEvent extends AbstractEvent {

}