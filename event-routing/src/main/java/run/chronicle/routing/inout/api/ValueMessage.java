package run.chronicle.routing.inout.api;

/**
 * This is a ValueMessage interface.
 * It provides a contract for any class that wants to send a {@link Value} message.
 * The purpose of this interface is to encapsulate the process of sending a value in a message.
 */
public interface ValueMessage {

    /**
     * This is a method contract for sending a {@link Value} message.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @param value the {@code Value} object that will be the content of the message
     */
    void value(Value value);
}
