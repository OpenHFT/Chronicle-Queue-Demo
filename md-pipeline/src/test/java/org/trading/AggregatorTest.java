/*
 * Copyright 2016-2025 chronicle.software
 */

package org.trading;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
public class AggregatorTest {
    public static void runTest(String path) {
        // Runs the test using the YamlTester against the AggregatorImpl class, passing the path to the YAML file
        net.openhft.chronicle.wire.utils.YamlTester yt = net.openhft.chronicle.wire.utils.YamlTester.runTest(AggregatorImpl.class, path);
        // Asserts that the expected state defined in the YAML file matches the actual state obtained from running the test
        assertEquals(yt.expected(), yt.actual());
    }

    @Test
    public void strategy() {
        // Running the test case defined in the "aggregator" YAML file
        runTest("aggregator");
    }
}