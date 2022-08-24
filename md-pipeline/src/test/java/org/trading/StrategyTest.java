/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StrategyTest {
    public static void runTest(String path) {
        YamlTester yt = YamlTester.runTest(StrategyImpl.class, path);
        assertEquals(yt.expected(), yt.actual());
    }

    @Test
    public void strategy() {
        runTest("strategy");
    }
}