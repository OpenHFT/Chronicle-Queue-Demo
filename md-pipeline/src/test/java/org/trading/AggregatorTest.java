/*
 * Copyright 2016-2025 chronicle.software
 */
package org.trading;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SuppressWarnings("deprecation")
public class AggregatorTest {
    private static net.openhft.chronicle.wire.utils.YamlTester runYamlTest(String path) {
        return net.openhft.chronicle.wire.utils.YamlTester.runTest(AggregatorImpl.class, path);
    }

    @Test
    public void strategy() {
        String path = "aggregator";
        net.openhft.chronicle.wire.utils.YamlTester yt = runYamlTest(path);
        assertEquals(yt.expected(), yt.actual(), () -> "AggregatorImpl YAML path=" + path);
    }
}
