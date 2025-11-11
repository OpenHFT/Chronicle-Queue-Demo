/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.queue.simple.avro;

import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;

public class OutputMain {
    public static void main(String[] args) throws IOException {
        AvroHelper avro = new AvroHelper();

        String path = "queue";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        ExcerptTailer tailer = queue.createTailer();

        while (true) {
            try (DocumentContext dc = tailer.readingDocument()) {
                if (dc.wire() == null)
                    break;

                GenericRecord user = avro.readFromIS(dc.wire().bytes().inputStream());
                System.out.println("Read: " + user);
            }
        }

        System.out.println("All done");
    }
}
