package net.openhft.chronicle.queue.simple.translator;

/**
 * An interface for consuming messages.
 * Implementations of this interface should define how to process or handle a given message.
 *
 * Created by catherine on 26/07/2016.
 */
public interface MessageConsumer {

    /**
     * Handles or processes the given message.
     *
     * @param text the message to be processed
     */
    void onMessage(String text);
}
