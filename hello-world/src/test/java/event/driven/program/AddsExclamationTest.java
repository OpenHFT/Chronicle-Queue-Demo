package event.driven.program;

import net.openhft.chronicle.wire.utils.YamlTester;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HellAddsExclamationTest {
    @Test
    public void say() {
        YamlTester yt = YamlTester.runTest(AddsExclamation.class, "says");
        assertEquals(yt.expected(), yt.actual());
    }
}
