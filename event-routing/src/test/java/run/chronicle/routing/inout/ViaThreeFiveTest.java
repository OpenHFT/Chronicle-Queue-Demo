package run.chronicle.routing.inout;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

// ViaThreeFiveTest is a test class for testing the ViaThreeFive class.
@SuppressWarnings("deprecation")
public class ViaThreeFiveTest {

    // The `via` method is a unit test for the `via` method in the ViaThreeFive class.
    @Test
    public void via() {
        // yt is an instance of YamlTester. The `runTest` method runs a test on the ViaThreeFive class with the input "three-five".
        final net.openhft.chronicle.wire.utils.YamlTester yt = net.openhft.chronicle.wire.utils.YamlTester.runTest(ViaThreeFive.class, "three-five");

        // Asserts that the expected result is equal to the actual result.
        // The `replace` method replaces any occurrences of "---\n---" in the actual result with "---".
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"));
    }
}