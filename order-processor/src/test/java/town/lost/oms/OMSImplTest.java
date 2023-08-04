/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms;

import net.openhft.chronicle.core.time.SetTimeProvider;
import net.openhft.chronicle.core.time.SystemTimeProvider;
import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Test class for OMSImpl.
 * The OMSImplTest runs tests for each method in OMSImpl class.
 * The test data is read from specified files and the actual output is compared against expected output.
 */
public class OMSImplTest {

    /**
     * Method to run a test.
     * This method sets up the time, runs the test, and asserts the expected and actual output are equal.
     *
     * @param path the path to the file containing the test data
     */
    public static void runTest(String path) {
        try {
            // Setup time
            SystemTimeProvider.CLOCK = new SetTimeProvider("2019-12-03T09:54:37.345678")
                    .advanceMicros(1);

            // Run test
            YamlTester yt = YamlTester.runTest(OMSImpl.class, path);

            // Assert expected and actual output are equal
            assertEquals(yt.expected(), yt.actual());
        } finally {
            // Reset clock
            SystemTimeProvider.CLOCK = SystemTimeProvider.INSTANCE;
        }
    }

    /**
     * Test case for new order single.
     */
    @Test
    public void newOrderSingle() {
        // Run test for new order single
        runTest("newOrderSingle");
    }

    /**
     * Test case for cancel order request.
     */
    @Test
    public void cancelOrderRequest() {
        // Run test for cancel order request
        runTest("cancelOrderRequest");
    }

    /**
     * Test case for cancel all orders.
     */
    @Test
    public void cancelAll() {
        // Run test for cancel all orders
        runTest("cancelAll");
    }
}
