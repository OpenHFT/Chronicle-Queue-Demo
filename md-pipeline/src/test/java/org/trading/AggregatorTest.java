/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AggregatorTest {
    public static void runTest(String path) {
        // Runs the test using the YamlTester against the AggregatorImpl class, passing the path to the YAML file
        YamlTester yt = YamlTester.runTest(AggregatorImpl.class, path);
        // Asserts that the expected state defined in the YAML file matches the actual state obtained from running the test
        assertEquals(yt.expected(), yt.actual());
    }

    @Test
    public void strategy() {
        // Running the test case defined in the "aggregator" YAML file
        runTest("aggregator");
    }
}