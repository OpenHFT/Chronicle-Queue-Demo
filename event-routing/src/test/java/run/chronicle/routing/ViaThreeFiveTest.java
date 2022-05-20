package run.chronicle.routing;

import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ViaThreeFiveTest {
    @Test
    public void via() {
        final YamlTester yt = YamlTester.runTest(ViaThreeFive.class, "three-five");
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"));
    }
}