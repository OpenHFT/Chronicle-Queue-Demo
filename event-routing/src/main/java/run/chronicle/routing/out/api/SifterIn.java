//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

package run.chronicle.routing.out.api;

import run.chronicle.routing.inout.api.Value;

/**
 * This is a SifterIn interface.
 * It provides a contract for any class that needs to process a Value object.
 * The purpose of this interface is to encapsulate the operation on a Value object.
 *
 * @since 2023-07-29
 */
public interface SifterIn {

    /**
     * This method provides a contract for processing a Value object.
     * Any class implementing this interface will provide its own implementation of this method.
     *
     * @param value A Value object to be processed
     */
    void value(Value value);
}
