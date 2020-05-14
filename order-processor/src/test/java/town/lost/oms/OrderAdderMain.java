/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.Mocker;
import net.openhft.chronicle.core.pool.ClassAliasPool;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.queue.ChronicleQueue;
import net.openhft.chronicle.queue.RollCycles;
import net.openhft.chronicle.queue.impl.single.SingleChronicleQueueBuilder;
import net.openhft.chronicle.wire.Base85LongConverter;
import town.lost.oms.api.OMSIn;
import town.lost.oms.dto.BuySell;
import town.lost.oms.dto.NewOrderSingle;
import town.lost.oms.dto.OrderType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class OrderAdderMain {
    public static void main(String[] args) throws IOException {
        ClassAliasPool.CLASS_ALIASES.addAlias(NewOrderSingle.class);
        try (ChronicleQueue q = SingleChronicleQueueBuilder.binary("in").rollCycle(RollCycles.TEST8_DAILY).build()) {
            OMSIn in = q.acquireAppender().methodWriter(OMSIn.class);
            OMSIn in2 = Mocker.logging(OMSIn.class, "in - ", System.out);

            NewOrderSingle nos = new NewOrderSingle()
                    .sender(toLong("sender"))
                    .target(toLong("target"))
                    .transactTime(now())
                    .sendingTime(now())
                    .orderQty(1)
                    .ordType(OrderType.market)
                    .side(BuySell.buy)
                    .symbol(toLong("EURUSD"));

            System.out.println("Hit blank line to add an order, anything else to exit");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            int counter = 0;
            while ("".equals(br.readLine())) {
                nos.clOrdID(Long.toString(counter++));
                in.newOrderSingle(nos);
                in2.newOrderSingle(nos);
            }
        }
        System.out.println("Finished");
    }

    static long now() {
        return SystemTimeProvider.INSTANCE.currentTimeMicros();
    }

    static long toLong(String s) {
        return Base85LongConverter.INSTANCE.parse(s);
    }
}
