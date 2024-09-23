package run.chronicle.routing.out;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// SifterImplTest is a test class for testing the SifterImpl class.
@SuppressWarnings("deprecation")
public class SifterImplTest {

    // The `value` method is a unit test for the `value` method in the SifterImpl class.
    @Test
    public void value() {
        // yt is an instance of YamlTester. The `runTest` method runs a test on the SifterImpl class with the input "sifter".
        final net.openhft.chronicle.wire.utils.YamlTester yt = net.openhft.chronicle.wire.utils.YamlTester.runTest(SifterImpl.class, "sifter");

        // Asserts that the expected result is equal to the actual result.
        // The `replace` method replaces any occurrences of "---\n---" in the actual result with "---".
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"));
    }
}