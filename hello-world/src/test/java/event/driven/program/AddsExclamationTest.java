package event.driven.program;

import event.driven.program.api.Says;
import net.openhft.chronicle.wire.TextMethodTester;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class AddsExclamationTest {
    @Test
    public void say() throws IOException {
        TextMethodTester<Says> tester = new TextMethodTester<>(
                "says/in.yaml",
                AddsExclamation::new,
                Says.class,
                "says/out.yaml");
        tester.run();
        assertEquals(tester.expected(), tester.actual());
    }
}