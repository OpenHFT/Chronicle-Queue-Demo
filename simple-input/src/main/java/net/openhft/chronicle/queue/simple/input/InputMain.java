//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package net.openhft.chronicle.queue.simple.input;

import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import java.util.Scanner;

/**
 * Created by catherine on 17/07/2016.
 */
public class InputMain {
    public static void main(String[] args) {
        // Allow the queue path to be specified via command-line arguments
        String path = (args.length > 0) ? args[0] : "queue";

        // Use try-with-resources for proper resource management
        try (ChronicleQueue queue = ChronicleQueue.single(path);
             Scanner scanner = new Scanner(System.in)) {

            ExcerptAppender appender = queue.createAppender();
            System.out.println("Starting InputMain. Type your input (empty line to exit).");

            while (true) {
                System.out.print("Input> ");
                String line = scanner.nextLine();
                if (line.isEmpty()) {
                    break;
                }
                appender.writeText(line);
                System.out.println("Written to queue: " + line);
            }
            System.out.println("... bye.");
        }
    }
}
