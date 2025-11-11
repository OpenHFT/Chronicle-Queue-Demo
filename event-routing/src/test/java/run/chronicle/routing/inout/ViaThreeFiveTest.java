/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.routing.inout;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// ViaThreeFiveTest is a test class for testing the ViaThreeFive class.
@SuppressWarnings("deprecation")
public class ViaThreeFiveTest {

    // The `via` method is a unit test for the `via` method in the ViaThreeFive class.
    @Test
    public void via() {
        doTest("three-five");
    }

    @Test
    public void values() {
        doTest("three-five-values");
    }

    private static void doTest(String path) {
        // yt is an instance of YamlTester. The `runTest` method runs a test on the ViaThreeFive class with the input.
        final net.openhft.chronicle.wire.utils.YamlTester yt = net.openhft.chronicle.wire.utils.YamlTester.runTest(ViaThreeFive.class, path);

        // Asserts that the expected result is equal to the actual result.
        // The `replace` method replaces any occurrences of "---\n---" in the actual result with "---".
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"));
    }
}