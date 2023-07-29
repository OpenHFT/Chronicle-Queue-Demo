package run.chronicle.routing.inout.api;

/**
 * This is a ValueMessage interface.
 * It provides a contract for any class that wants to send a value message.
 * The purpose of this interface is to encapsulate the process of sending a value in a message.
 *
 * @since 2023-07-29
 */
public interface ValueMessage {

    /**
     * This is a method contract for sending a Value message.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @param value A Value object that will be the content of the message
     */
    void value(Value value);
}
