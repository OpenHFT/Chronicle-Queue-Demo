/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.queue.simple.avro;

import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.DocumentContext;
import org.apache.avro.generic.GenericRecord;

import java.io.IOException;

public class InputMain {
    public static void main(String[] args) throws IOException {
        AvroHelper avro = new AvroHelper();

        String path = "queue";
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();
        ExcerptAppender appender = queue.createAppender();

        try (DocumentContext dc = appender.writingDocument()) {
            GenericRecord user = avro.getGenericRecord();
            user.put("name", "Alyssa");
            user.put("favorite_number", 256);
            avro.writeToOS(user, dc.wire().bytes().outputStream());
        }

        try (DocumentContext dc = appender.writingDocument()) {
            GenericRecord user = avro.getGenericRecord();
            user.put("name", "Ben");
            user.put("favorite_number", 7);
            user.put("favorite_colour", "red");
            avro.writeToOS(user, dc.wire().bytes().outputStream());
        }

        System.out.println("2 records written.");
    }
}
