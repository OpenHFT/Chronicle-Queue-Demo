/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.bytes.MethodReader;
import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.util.Mocker;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.queue.rollcycles.TestRollCycles;
import town.lost.oms.api.OMSIn;

/**
 * Main class to view orders.
 */
public class OrderViewerMain {
    /**
     * The entry point of the application.
     *
     * @param args the input arguments (none expected)
     */
    public static void main(String[] args) {
        // Inform the user that the program is waiting for messages
        System.out.println("\nWaiting for messages");

        // Establish connection with the queue
        try (ChronicleQueue q = SingleChronicleQueueBuilder.binary("in")
                .rollCycle(TestRollCycles.TEST8_DAILY)
                .build()) {

            // Create a logging mock for OMSIn
            OMSIn logging = Mocker.logging(OMSIn.class, "read - ", System.out);

            // Create a MethodReader from the tail of the queue
            MethodReader reader = q.createTailer().methodReader(logging);

            while (true) {
                // Read one message from the queue, pause if no message was read
                if (!reader.readOne()) {
                    Jvm.pause(50);
                }
            }
        }
    }
}
