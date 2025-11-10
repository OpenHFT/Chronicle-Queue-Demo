//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package net.openhft.chronicle.queue.simple.translator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link SimpleTranslator}.
 */
class SimpleTranslator2Test {

    private MessageConsumer mockConsumer;
    private SimpleTranslator translator;

    @BeforeEach
    void setUp() {
        mockConsumer = mock(MessageConsumer.class);
        translator = new SimpleTranslator(mockConsumer);
    }

    @Test
    @DisplayName("Translate single word")
    void translateSingleWord() {
        translator.onMessage("hello");
        verify(mockConsumer).onMessage("salut");
    }

    @Test
    @DisplayName("Translate multiple words")
    void translateMultipleWords() {
        translator.onMessage("hello apple");
        verify(mockConsumer).onMessage("salut pomme");
    }

    @Test
    @DisplayName("Translate with mixed case")
    void translateMixedCase() {
        translator.onMessage("Hello Apple");
        verify(mockConsumer).onMessage("salut pomme");
    }

    @Test
    @DisplayName("Handle message with no translations")
    void handleNoTranslations() {
        translator.onMessage("good morning");
        verify(mockConsumer).onMessage("good morning");
    }

    @Test
    @DisplayName("Handle null input")
    void handleNullInput() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            translator.onMessage(null);
        });
        assertEquals("Input text cannot be null", exception.getMessage());
    }

    @Test
    @DisplayName("Ensure translations do not alter unrelated words")
    void doNotAlterUnrelatedWords() {
        translator.onMessage("I love programming");
        verify(mockConsumer).onMessage("I amour programming");
    }

    @Test
    @DisplayName("Ensure only whole words are translated")
    void translateWholeWordsOnly() {
        translator.onMessage("hellothere");
        verify(mockConsumer).onMessage("hellothere");
    }

    @Test
    @DisplayName("Translate multiple occurrences of the same word")
    void translateMultipleOccurrences() {
        translator.onMessage("hello hello");
        verify(mockConsumer).onMessage("salut salut");
    }

    @Test
    @DisplayName("Translate words with punctuation")
    void translateWithPunctuation() {
        translator.onMessage("hello, world!");
        verify(mockConsumer).onMessage("salut, world!");
    }
}
