/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OMSImplTest {
    public static void runTest(String path) {
        try {
            SystemTimeProvider.CLOCK = new SetTimeProvider("2019-12-03T09:54:37.345678")
                    .advanceMicros(1);
            YamlTester yt = YamlTester.runTest(OMSImpl.class, path);
            assertEquals(yt.expected(), yt.actual());
        } finally {
            SystemTimeProvider.CLOCK = SystemTimeProvider.INSTANCE;
        }
    }

    @Test
    public void newOrderSingle() {
        runTest("newOrderSingle");
    }

    @Test
    public void cancelOrderRequest() {
        runTest("cancelOrderRequest");
    }

    @Test
    public void cancelAll() {
        runTest("cancelAll");
    }
}