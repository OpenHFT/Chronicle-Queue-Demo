/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.api;

import org.trading.dto.MarketDataSnapshot;

/**
 * This is the {@code AggregatorOut} interface, which defines a contract for handling outgoing market data snapshots (MDS).
 *
 * Implementing classes should provide a concrete implementation for processing or forwarding the market data snapshots.
 */
public interface AggregatorOut {

    /**
     * This method handles the provided {@link MarketDataSnapshot} object, typically processing or forwarding the snapshot.
     *
     * @param mds A {@link MarketDataSnapshot} object representing a market data snapshot to be handled.
     */
    void marketDataSnapshot(MarketDataSnapshot mds);
}
