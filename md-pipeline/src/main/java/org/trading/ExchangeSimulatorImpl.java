/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import org.trading.api.AggregatorIn;
import org.trading.dto.BuySell;
import org.trading.dto.MarketDataIncrement;
import org.trading.dto.NewOrderSingle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;

public class ExchangeSimulatorImpl {
    public static void main(String[] args) throws IOException {
        ClassAliasPool.CLASS_ALIASES.addAlias(NewOrderSingle.class);
        try (ChronicleQueue q = SingleChronicleQueueBuilder.binary("agg-in").build()) {
            AggregatorIn in = q.acquireAppender().methodWriter(AggregatorIn.class);

            double mid = 23418.80;
            MarketDataIncrement mdi = new MarketDataIncrement()
                    .orderQty(1)
                    .symbol("BTCUSD");

            System.out.println("\nHit blank line to send market data, anything else to exit");
            Random random = new Random();
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            while ("".equals(br.readLine())) {
                mid += random.nextDouble() - 0.5;
                mdi.transactTime(SystemTimeProvider.INSTANCE.currentTimeMicros())
                        .side(random.nextBoolean() ? BuySell.buy : BuySell.sell);
                double halfSpread = random.nextDouble();
                halfSpread *= mdi.side().direction;
                mdi.price(mid + halfSpread);
                in.mdi(mdi);
            }
        }
        System.out.println("Finished");
    }
}
