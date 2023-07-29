/**
 * This package contains classes for a simple translation system using Chronicle Queues.
 * <p>
 * The main classes in this package include:
 * <ul>
 *     <li>{@link net.openhft.chronicle.queue.simple.translator.MessageConsumer} - An interface that defines a consumer
 *     that can process messages.</li>
 *     <li>{@link net.openhft.chronicle.queue.simple.translator.SimpleTranslator} - A class that translates text messages
 *     from English to French and then forwards them to another MessageConsumer.</li>
 *     <li>{@link net.openhft.chronicle.queue.simple.translator.TranslatorMain} - The main class that sets up a
 *     SimpleTranslator to translate messages from an English queue to a French queue.</li>
 *     <li>{@link net.openhft.chronicle.queue.simple.translator.PrintQueueMain} - The main class that prints out the contents
 *     of the English and French queues.</li>
 *     <li>{@link net.openhft.chronicle.queue.simple.translator.OutputMain} - The main class that reads messages from the
 *     French queue and outputs them to the console.</li>
 *     <li>{@link net.openhft.chronicle.queue.simple.translator.InputMain} - The main class that takes user input and writes
 *     it to the English queue.</li>
 * </ul>
 * </p>
 *
 * @since 1.0
 */
package net.openhft.chronicle.queue.simple.translator;
