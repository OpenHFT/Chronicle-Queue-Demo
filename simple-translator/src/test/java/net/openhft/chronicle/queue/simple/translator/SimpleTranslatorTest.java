package net.openhft.chronicle.queue.simple.translator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by catherine on 26/07/2016.
 */
public class SimpleTranslatorTest {
    @Test
    public void onMessage() throws Exception {
        StringBuilder sb = new StringBuilder();

        SimpleTranslator trans = new SimpleTranslator(m -> sb.append(m).append(","));

        trans.onMessage("hello apple");
        trans.onMessage("bye now");
        trans.onMessage("banana");

        assertEquals("salut pomme," +
                        "salut now," +
                        "banane,",
                sb.toString());
    }

    @Test
    public void onMessage2() throws Exception {
        StringBuilder sb = new StringBuilder();

        SimpleTranslator trans = new SimpleTranslator(sb::append);

        doTest(trans, sb, "hello apple", "salut pomme");
        doTest(trans, sb, "bye now", "salut now");
    }

    private void doTest(SimpleTranslator trans, StringBuilder sb, String in, String out) {
        sb.setLength(0);

        trans.onMessage(in);

        assertEquals(out, sb.toString());
    }
}