/*
 * Copyright 2016-2025 chronicle.software
 */
package org.trading;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
public class StrategyTest {
    public static void runTest(String path) {
        // Runs the test using the YamlTester against the StrategyImpl class, passing the path to the YAML file
        net.openhft.chronicle.wire.utils.YamlTester yt = net.openhft.chronicle.wire.utils.YamlTester.runTest(StrategyImpl.class, path);
        // Asserts that the expected state defined in the YAML file matches the actual state obtained from running the test
        assertEquals(yt.expected(), yt.actual());
    }

    @Test
    public void strategy() {
        // Running the test case defined in the "strategy" YAML file
        runTest("strategy");
    }
}