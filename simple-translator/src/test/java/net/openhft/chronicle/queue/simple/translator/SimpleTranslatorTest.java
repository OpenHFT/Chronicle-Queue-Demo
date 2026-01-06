package net.openhft.chronicle.queue.simple.translator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link SimpleTranslator}.
 *
 * <p>Ensures that messages are correctly translated from English to French.</p>
 *
 * Created by catherine on 26/07/2016.
 */
public class SimpleTranslatorTest {

    @Test
    public void onMessage() {
        // Create a StringBuilder to collect the translated messages
        StringBuilder sb = new StringBuilder();

        // Create a SimpleTranslator with a MessageConsumer that appends translated messages to sb
        SimpleTranslator trans = new SimpleTranslator(m -> sb.append(m).append(","));

        // Translate and collect some messages
        trans.onMessage("hello apple");
        trans.onMessage("bye now");
        trans.onMessage("banana");

        // Check that the collected translations match the expected translations
        assertEquals("salut pomme," +
                        "salut now," +
                        "banane,",
                sb.toString(),
                "translate multiple messages");
    }

    @Test
    public void onMessage2() {
        // Create a StringBuilder to collect the translated messages
        StringBuilder sb = new StringBuilder();

        // Create a SimpleTranslator with a MessageConsumer that appends translated messages to sb
        SimpleTranslator trans = new SimpleTranslator(sb::append);

        assertEquals("salut pomme", translate(trans, sb, "hello apple"), "translate 'hello apple'");
        assertEquals("salut now", translate(trans, sb, "bye now"), "translate 'bye now'");
    }

    /**
     * Helper method that translates a single message and returns the output.
     *
     * @param trans the SimpleTranslator to use
     * @param sb the StringBuilder to collect the translation
     * @param input the input message
     * @return the translated text
     */
    private static String translate(SimpleTranslator trans, StringBuilder sb, String input) {
        // Reset sb to an empty state
        sb.setLength(0);

        // Translate the input message
        trans.onMessage(input);
        return sb.toString();
    }
}
