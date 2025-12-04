/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

/**
 * A Main class that sets up a translator for messages between two queues.
 * This class reads messages from an English queue, translates them into French,
 * and then writes the translated messages into a French queue.
 *
 * Created by catherine on 26/07/2016.
 */
public class TranslatorMain {
    public static void main(String[] args) throws InterruptedException {
        // Path to the queue for French messages
        String pathfr = "queue-fr";

        // Create a SingleChronicleQueue for French messages
        SingleChronicleQueue queuefr = SingleChronicleQueueBuilder.binary(pathfr).build();

        // Create a MessageConsumer that will append messages to the French queue
        MessageConsumer messageConsumer = queuefr.createAppender().methodWriter(MessageConsumer.class);

        // Create a SimpleTranslator that will translate messages to French
        MessageConsumer simpleTranslator = new SimpleTranslator(messageConsumer);

        // Path to the queue for English messages
        String path_en = "queue-en";

        // Create a SingleChronicleQueue for English messages
        SingleChronicleQueue queue_en = SingleChronicleQueueBuilder.binary(path_en).build();

        // Create a MethodReader that will read messages from the English queue
        MethodReader methodReader = queue_en.createTailer().methodReader(simpleTranslator);

        // Continuously read and translate messages from the English queue to the French queue
        while (true) {
            // Read and translate one message, if available
            // If no message was available, pause for a short time to avoid busy-waiting
            if (!methodReader.readOne())
                Thread.sleep(10);
        }
    }
}
