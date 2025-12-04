/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package event.driven.program;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
public class AddsExclamationTest {
    // Test method for the 'say' functionality in the AddsExclamation class
    @Test
    public void say() {
        // Create a YamlTester object by running a test on AddsExclamation class with the provided data file 'says'
        net.openhft.chronicle.wire.utils.YamlTester yt = net.openhft.chronicle.wire.utils.YamlTester.runTest(AddsExclamation.class, "says");

        // Assert that the expected value matches the actual value returned by the 'say' method
        assertEquals(yt.expected(), yt.actual());
    }
}
