//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package net.openhft.chronicle.queue.simple.input;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.queue.ExcerptTailer;
import net.openhft.chronicle.queue.ChronicleQueue;


/**
 * Created by catherine on 17/07/2016.
 */
public class OutputMain {
    public static void main(String[] args) {
        // Allow the queue path to be specified via command-line arguments
        String path = (args.length > 0) ? args[0] : "queue";

        // Use try-with-resources for proper resource management
        try (ChronicleQueue queue = ChronicleQueue.single(path)) {
            ExcerptTailer tailer = queue.createTailer();

            System.out.println("Starting OutputMain. Waiting for messages...");

            while (true) {
                String text = tailer.readText();
                if (text == null) {
                    // Pause briefly to avoid busy-waiting
                    Jvm.pause(10);
                } else {
                    System.out.println("Read from queue: " + text);
                }
            }
        }
    }
}
