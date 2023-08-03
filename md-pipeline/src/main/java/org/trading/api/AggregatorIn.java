/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
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
