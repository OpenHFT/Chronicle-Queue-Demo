/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.api;

import org.trading.dto.NewOrderSingle;

/**
 * This is the {@code OMSIn} interface, defining a contract for receiving and handling new single orders within the Order Management System (OMS).
 *
 * Implementing classes should provide a concrete implementation for processing or forwarding the new single order messages.
 *
 * @since 2023-07-30
 */
public interface OMSIn {

    /**
     * This method handles the provided {@link NewOrderSingle} object, typically processing or forwarding the new single order.
     *
     * @param nos A {@link NewOrderSingle} object representing a new single order to be handled.
     */
    void newOrderSingle(NewOrderSingle nos);
}
