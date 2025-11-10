//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package net.openhft.chronicle.queue.simple.translator;

import net.openhft.chronicle.queue.impl.single.SingleChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;

import java.util.Scanner;

/**
 * A Main class that takes user input from the console and writes it to a queue ("queue-en").
 * Continues to accept input until an empty line is entered by the user.
 *
 * Created by catherine on 26/07/2016.
 */
public class InputMain {
    public static void main(String[] args) {
        // Path to the queue where input will be stored
        String path_en = "queue-en";

        // Create a SingleChronicleQueue for storing the input
        SingleChronicleQueue queue_en = SingleChronicleQueueBuilder.binary(path_en).build();

        // Create a MessageConsumer that will append messages to the queue
        MessageConsumer messageConsumer = queue_en.createAppender().methodWriter(MessageConsumer.class);

        // Create a Scanner to read input from the console
        Scanner read = new Scanner(System.in);

        // Continuously read input from the console and write it to the queue
        while (true) {
            System.out.println("type something");
            String line = read.nextLine();

            // Exit the loop if the input line is empty
            if (line.isEmpty())
                break;

            // Write the input to the queue
            messageConsumer.onMessage(line);
        }

        // Inform the user that the program has finished
        System.out.println("... bye.");
    }
}
