/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package run.chronicle.routing.inout.api;

/**
 * Describes the contract for any class that handles
 * a 'Value' message. The system will call `value(...)`
 * whenever a new message is received or processed.
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
