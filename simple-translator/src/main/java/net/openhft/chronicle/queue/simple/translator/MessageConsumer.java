package net.openhft.chronicle.queue.simple.translator;

/**
 * Created by catherine on 26/07/2016.
 */
public interface MessageConsumer {
    void onMessage(String text);
}
