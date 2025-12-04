/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
/**
 * Provides a simple translation system leveraging Chronicle Queues for message handling.
 *
 * <p>This package facilitates the translation of text messages from English to French using a
 * {@link net.openhft.chronicle.queue.simple.translator.SimpleTranslator}. The system is designed
 * to integrate seamlessly with Chronicle Queues, enabling efficient message processing and
 * translation in a queue-based architecture.</p>
 *
 * <p>The primary components of this package include:</p>
 *
 * <ul>
 *     <li>
 *         <strong>{@link net.openhft.chronicle.queue.simple.translator.MessageConsumer}</strong>:
 *         An interface defining the contract for consuming and processing translated messages.
 *     </li>
 *     <li>
 *         <strong>{@link net.openhft.chronicle.queue.simple.translator.SimpleTranslator}</strong>:
 *         Implements the translation logic from English to French and delegates the translated messages
 *         to a specified {@link net.openhft.chronicle.queue.simple.translator.MessageConsumer}.
 *     </li>
 *     <li>
 *         <strong>{@link net.openhft.chronicle.queue.simple.translator.TranslatorMain}</strong>:
 *         The entry point that initialises and configures a {@link SimpleTranslator} to bridge messages
 *         between the English and French queues.
 *     </li>
 *     <li>
 *         <strong>{@link net.openhft.chronicle.queue.simple.translator.PrintQueueMain}</strong>:
 *         A utility class responsible for displaying the contents of both English and French queues for
 *         monitoring and debugging purposes.
 *     </li>
 *     <li>
 *         <strong>{@link net.openhft.chronicle.queue.simple.translator.OutputMain}</strong>:
 *         Handles the retrieval of translated messages from the French queue and outputs them to the console.
 *     </li>
 *     <li>
 *         <strong>{@link net.openhft.chronicle.queue.simple.translator.InputMain}</strong>:
 *         Manages user input by capturing messages and writing them to the English queue for subsequent translation.
 *     </li>
 * </ul>
 * 
 * <p>Together, these components provide a robust framework for translating and managing messages
 * within a Chronicle Queue-based system.</p>
 */
package net.openhft.chronicle.queue.simple.translator;
