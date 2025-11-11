/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */

package town.lost.oms;

import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.core.util.Mocker;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.queue.rollcycles.TestRollCycles;
import net.openhft.chronicle.wire.converter.ShortText;
import town.lost.oms.api.OMSIn;
import town.lost.oms.dto.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The {@code OrderAdderMain} class is a utility application that allows users to add new orders to the Order Management System (OMS).
 *
 * <p>This class connects to a Chronicle Queue and writes {@link NewOrderSingle} messages to it.
 * Users can interactively add orders by hitting the Enter key. The application will prompt the user
 * and continue to accept orders until any non-empty input is entered.
 */
public class OrderAdderMain {

    /**
     * The entry point of the application.
     *
     * @param args the input arguments (none expected)
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
                    .sender(fromShortText("sender"))
                    .target(fromShortText("target"))
                    .transactTime(now())
                    .sendingTime(now())
                    .account(1)
                    .timeInForce(TimeInForce.GTC)
                    .currency(Ccy.USD)
                    .orderQty(1)
                    .ordType(OrderType.MARKET)
                    .side(Side.BUY)
                    .symbol(fromShortText("EURUSD"));

            // Inform the user to add an order
            System.out.println("\nHit blank line to add an order, anything else to exit");

            // Initialise a BufferedReader to read user input
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int orderCounter = 0;
            while ("".equals(br.readLine())) {
                // For each blank line read, add a new order
                nos.clOrdID(Long.toString(orderCounter++));

                // Send the new order
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
     * Returns the current system time in nanoseconds.
     *
     * @return the current time in nanoseconds
     */
    static long now() {
        return SystemTimeProvider.INSTANCE.currentTimeNanos();
    }

    /**
     * Converts a string to a long using base85 encoding.
     *
     * @param s the string to convert
     * @return the long representation of the string
     */
    static long fromShortText(String s) {
        return ShortText.INSTANCE.parse(s);
    }
}
