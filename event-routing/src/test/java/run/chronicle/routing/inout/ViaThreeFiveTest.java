package run.chronicle.routing.inout;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

// ViaThreeFiveTest is a test class for testing the ViaThreeFive class.
@SuppressWarnings("deprecation")
public class ViaThreeFiveTest {

    // The `via` method is a unit test for the `via` method in the ViaThreeFive class.
    @Test
    public void via() {
        String path = "three-five";
        net.openhft.chronicle.wire.utils.YamlTester yt = runYamlTest(path);
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"), () -> "ViaThreeFive YAML path=" + path);
    }

    @Test
    public void values() {
        String path = "three-five-values";
        net.openhft.chronicle.wire.utils.YamlTester yt = runYamlTest(path);
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"), () -> "ViaThreeFive YAML path=" + path);
    }

    private static net.openhft.chronicle.wire.utils.YamlTester runYamlTest(String path) {
        return net.openhft.chronicle.wire.utils.YamlTester.runTest(ViaThreeFive.class, path);
    }
}
