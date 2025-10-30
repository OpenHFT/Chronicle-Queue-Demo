package net.openhft.chronicle.queue.simple.avro;

import java.io.IOException;
import java.util.Objects;

import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import net.openhft.chronicle.wire.Wire;
import org.apache.avro.generic.GenericRecord;

public class OutputMain {
    public static void main(String[] args) throws IOException {
        AvroHelper avro = new AvroHelper();

        String path = "queue";
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptTailer tailer = queue.createTailer();

            while (true) {
                try (DocumentContext dc = tailer.readingDocument()) {
                    Wire wire = dc.wire();
                    if (wire == null) {
                        break;
                    }
                    GenericRecord user = avro.readFromIS(Objects.requireNonNull(wire.bytes(), "Wire bytes missing")
                            .inputStream());
                    System.out.println("Read: " + user);
                }
            }
        }

        System.out.println("All done");
    }
}
