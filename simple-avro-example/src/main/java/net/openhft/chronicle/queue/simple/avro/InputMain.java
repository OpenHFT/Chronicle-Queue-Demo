package net.openhft.chronicle.queue.simple.avro;

import java.io.IOException;
import java.util.Objects;

import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import net.openhft.chronicle.wire.Wire;
import org.apache.avro.generic.GenericRecord;

public class InputMain {
    public static void main(String[] args) throws IOException {
        AvroHelper avro = new AvroHelper();

        String path = "queue";
        try (SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build()) {
            ExcerptAppender appender = queue.createAppender();

            try (DocumentContext dc = appender.writingDocument()) {
                GenericRecord user = avro.getGenericRecord();
                user.put("name", "Alyssa");
                user.put("favorite_number", 256);
                Wire wire = Objects.requireNonNull(dc.wire(), "Writing document must provide a wire");
                avro.writeToOS(user, wire.bytes().outputStream());
            }

            try (DocumentContext dc = appender.writingDocument()) {
                GenericRecord user = avro.getGenericRecord();
                user.put("name", "Ben");
                user.put("favorite_number", 7);
                user.put("favorite_colour", "red");
                Wire wire = Objects.requireNonNull(dc.wire(), "Writing document must provide a wire");
                avro.writeToOS(user, wire.bytes().outputStream());
            }
        }

        System.out.println("2 records written.");
    }
}
