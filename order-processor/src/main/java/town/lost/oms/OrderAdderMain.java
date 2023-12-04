/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.core.util.Mocker;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.queue.rollcycles.TestRollCycles;
import net.openhft.chronicle.wire.converter.Base85;
import town.lost.oms.api.OMSIn;
import town.lost.oms.dto.BuySell;
import town.lost.oms.dto.NewOrderSingle;
import town.lost.oms.dto.OrderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Main class to add new orders.
 */
public class OrderAdderMain {
    /**
     * The entry point of the application.
     *
     * @param args the input arguments (none expected)
     * @throws IOException if an I/O error occurs
     */
    public static void main(String[] args) throws IOException {
        // Add NewOrderSingle class to the alias pool
        ClassAliasPool.CLASS_ALIASES.addAlias(NewOrderSingle.class);

        // Establish connection with the queue
        try (ChronicleQueue q = SingleChronicleQueueBuilder.binary("in")
                .rollCycle(TestRollCycles.TEST8_DAILY)
                .build();
             ExcerptAppender appender = q.createAppender()) {

            // Acquire the appender and write methods for OMSIn
            OMSIn in = appender.methodWriter(OMSIn.class);

            // Create a logging mock for OMSIn
            OMSIn in2 = Mocker.logging(OMSIn.class, "in - ", System.out);

            // Create a new order single
            NewOrderSingle nos = new NewOrderSingle()
                    .sender(toLong("sender"))
                    .target(toLong("target"))
                    .transactTime(now())
                    .sendingTime(now())
                    .orderQty(1)
                    .ordType(OrderType.market)
                    .side(BuySell.buy)
                    .symbol(toLong("EURUSD"));

            // Inform the user to add an order
            System.out.println("\nHit blank line to add an order, anything else to exit");

            // Initialize a BufferedReader to read user input
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int counter = 0;
            while ("".equals(br.readLine())) {
                // For each blank line read, add a new order
                nos.clOrdID(Long.toString(counter++));
                in.newOrderSingle(nos);
                in2.newOrderSingle(nos);
            }
        }
        // Inform the user that the program has finished running
        System.out.println("Finished");

        // Exit the program
        System.exit(0);
    }

    /**
     * Returns the current time in microseconds.
     *
     * @return the current time in microseconds
     */
    static long now() {
        return SystemTimeProvider.INSTANCE.currentTimeMicros();
    }

    /**
     * Converts a string to a long using base85 encoding.
     *
     * @param s the string to convert
     * @return the long value of the string
     */
    static long toLong(String s) {
        return Base85.INSTANCE.parse(s);
    }
}
