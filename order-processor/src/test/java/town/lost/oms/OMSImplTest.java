/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.Jvm;
import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.wire.TextMethodTester;
import org.junit.Test;
import town.lost.oms.api.OMSOut;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class OMSImplTest {
    public static void runTest(String path) {
        SystemTimeProvider.CLOCK = new SetTimeProvider("2019-12-03T09:54:37.345678")
                .advanceMicros(1);
        String input =  path + "/in.yaml";
        String output =  path + "/out.yaml";
        TextMethodTester tester = new TextMethodTester<>(input, OMSImpl::new, OMSOut.class, output);
        try {
            tester.run();
            assertEquals(tester.expected(), tester.actual());
        } catch (IOException e) {
            Jvm.rethrow(e);
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
}