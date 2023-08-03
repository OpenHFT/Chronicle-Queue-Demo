package event.driven.program;

import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AddsExclamationTest {
    // Test method for the 'say' functionality in the AddsExclamation class
    @Test
    public void say() {
        // Create a YamlTester object by running a test on AddsExclamation class with the provided data file 'says'
        YamlTester yt = YamlTester.runTest(AddsExclamation.class, "says");

        // Assert that the expected value matches the actual value returned by the 'say' method
        assertEquals(yt.expected(), yt.actual());
    }
}
