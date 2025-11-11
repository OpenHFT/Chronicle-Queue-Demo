/*
 * Copyright 2016-2025 chronicle.software
 */
package org.trading;

import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.ExcerptAppender;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.trading.api.AggregatorIn;
import org.trading.dto.BuySell;
import org.trading.dto.MarketDataIncrement;
import org.trading.dto.NewOrderSingle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

/**
 * This class, ExchangeSimulatorImpl, simulates an exchange by generating market data and sending it to an aggregator.
 * It utilizes the ChronicleQueue to manage data and demonstrates how market data increments are created, processed,
 * and sent through the system. It continues to send market data until the user decides to exit.
 */
public class ExchangeSimulatorImpl {

    /**
     * The main method of ExchangeSimulatorImpl, responsible for setting up the simulation and running it.
     * It allows the user to interactively send market data and control the simulation's operation.
     *
     * @param args Command-line arguments (not used in this implementation)
     * @throws IOException If an I/O error occurs during the process
     */
    public static void main(String[] args) throws IOException {
        // Registering the alias for NewOrderSingle class
        ClassAliasPool.CLASS_ALIASES.addAlias(NewOrderSingle.class);

        // Building the ChronicleQueue for the "agg-in" channel
        try (ChronicleQueue q = SingleChronicleQueueBuilder.binary("agg-in").build();
            ExcerptAppender appender = q.createAppender()) {
            // Acquiring the method writer for the AggregatorIn interface
            AggregatorIn in = appender.methodWriter(AggregatorIn.class);

            // Initializing market data increment with a starting value
            double mid = 23418.80;
            MarketDataIncrement mdi = new MarketDataIncrement()
                    .orderQty(1)
                    .symbol("BTCUSD");

            System.out.println("\nHit blank line to send market data, anything else to exit");
            Random random = new Random();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            // Main loop for the interactive simulation
            while ("".equals(br.readLine())) {
                // Simulating a small change in the mid-price
                mid += random.nextDouble() - 0.5;

                // Setting transaction time to the current time in microseconds
                mdi.transactTime(SystemTimeProvider.INSTANCE.currentTimeMicros())
                        .side(random.nextBoolean() ? BuySell.buy : BuySell.sell);
                        // Randomly selecting Buy or Sell

                // Randomly generating a half spread, adjusted based on the side of the trade
                double halfSpread = random.nextDouble();
                halfSpread *= mdi.side().direction;

                // Adjusting the price of the market data increment
                mdi.price(mid + halfSpread);

                // Sending the market data increment to the aggregator
                in.mdi(mdi);
            }
        }
        System.out.println("Finished");
    }
}
