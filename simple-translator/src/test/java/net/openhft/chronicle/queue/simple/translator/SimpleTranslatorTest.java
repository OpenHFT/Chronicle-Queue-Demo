/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.queue.simple.translator;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
                sb.toString());
    }

    @Test
    public void onMessage2() {
        // Create a StringBuilder to collect the translated messages
        StringBuilder sb = new StringBuilder();

        // Create a SimpleTranslator with a MessageConsumer that appends translated messages to sb
        SimpleTranslator trans = new SimpleTranslator(sb::append);

        // Translate and check some messages using the doTest helper method
        performTranslationTest(trans, sb, "hello apple", "salut pomme");
        performTranslationTest(trans, sb, "bye now", "salut now");
    }

    /**
     * Helper method that tests the translation of a single message.
     *
     * @param trans the SimpleTranslator to use
     * @param sb the StringBuilder to collect the translation
     * @param in the input message
     * @param out the expected translation
     */
    private void performTranslationTest(SimpleTranslator trans, StringBuilder sb, String in, String out) {
        // Reset sb to an empty state
        sb.setLength(0);

        // Translate the input message
        trans.onMessage(in);

        // Check that the translation matches the expected translation
        assertEquals(out, sb.toString());
    }
}
