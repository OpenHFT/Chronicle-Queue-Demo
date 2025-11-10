//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

/**
 * A Main class that reads messages from a queue ("queue-fr") and outputs them to the console.
 * It creates a MessageConsumer using a lambda expression to print the received messages.
 *
 * Created by catherine on 17/07/2016.
 */
public class OutputMain {
    public static void main(String[] args) throws InterruptedException {
        // Path to the queue from which messages will be read
        String path = "queue-fr";

        // Create a SingleChronicleQueue for reading the messages
        SingleChronicleQueue queue = SingleChronicleQueueBuilder.binary(path).build();

        // Create a MessageConsumer that will print the received messages to the console
        MessageConsumer messagePrinter = System.out::println;

        // Create a MethodReader that will read messages from the queue
        MethodReader methodReader = queue.createTailer().methodReader(messagePrinter);

        // Continuously read and print messages from the queue
        while (true) {
            // Read and print one message, if available
            // If no message was available, pause for a short time to avoid busy-waiting
            if (!methodReader.readOne())
                Thread.sleep(10);
        }
    }
}
