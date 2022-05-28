package run.chronicle.routing.out;

import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SifterImplTest {

    @Test
    public void value() {
        final YamlTester yt = YamlTester.runTest(SifterImpl.class, "sifter");
        assertEquals(yt.expected(), yt.actual().replace("---\n---", "---"));
    }
}