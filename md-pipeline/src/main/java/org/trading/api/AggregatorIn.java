//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/*
 * Copyright 2016-2025 chronicle.software
 */

package org.trading.api;

import org.trading.dto.MarketDataIncrement;

/**
 * This is the {@code AggregatorIn} interface, which defines a contract for handling incoming market data increments (MDI).
 *
 * Implementing classes should provide a concrete implementation for processing the market data increments.
 */
public interface AggregatorIn {

    /**
     * This method handles the provided {@link MarketDataIncrement} object, typically processing or forwarding the increment.
     *
     * @param mdi A {@link MarketDataIncrement} object representing a market data increment to be handled.
     */
    void mdi(MarketDataIncrement mdi);
}
