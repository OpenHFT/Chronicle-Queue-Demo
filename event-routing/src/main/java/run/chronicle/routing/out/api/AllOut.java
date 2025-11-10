//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.routing.out.api;

import run.chronicle.routing.inout.api.Value;

/**
 * This is an AllOut interface.
 * It provides a contract for any class that wants to send a value message using the 'value' method.
 * The purpose of this interface is to encapsulate the process of sending a value in a message.
 * The value being sent is an instance of the Value class from the run.chronicle.routing.inout.api package.
 */
public interface AllOut {

    /**
     * This method provides a contract for sending a Value message.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @param value A Value object that will be the content of the message
     */
    void value(Value value);
}
