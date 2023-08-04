/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import org.trading.api.AggregatorIn;
import org.trading.api.AggregatorOut;
import org.trading.dto.BuySell;
import org.trading.dto.MarketDataIncrement;
import org.trading.dto.MarketDataSnapshot;

import java.util.HashMap;
import java.util.Map;

/**
 * This is an implementation of the AggregatorIn interface, known as AggregatorImpl.
 * It maintains a map of market data snapshots and interfaces with an AggregatorOut object
 * to process and handle aggregated market data.
 */
public class AggregatorImpl implements AggregatorIn {

    // A map holding market data snapshots, keyed by a long identifier
    private final Map<Long, MarketDataSnapshot> md = new HashMap<>();

    // The output interface for handling aggregated data
    private final AggregatorOut out;

    /**
     * Constructor for the AggregatorImpl class.
     * Initializes the AggregatorOut instance for output handling.
     *
     * @param out An implementation of the AggregatorOut interface for handling aggregated data
     */
    public AggregatorImpl(AggregatorOut out) {
        this.out = out;
    }

    /**
     * The main method to start the AggregatorImpl.
     * Invokes the Runner class to run the aggregator with specific input and output channels.
     *
     * @param args Command-line arguments (not used in this implementation)
     */
    public static void main(String[] args) {
        Runner.run("agg-in", "agg-out", AggregatorOut.class, AggregatorImpl::new);
    }

    @Override
    public void mdi(MarketDataIncrement mdi) {
        MarketDataSnapshot aggregated = md.computeIfAbsent(mdi.symbol(), MarketDataSnapshot::new);
        // trivially simple aggregation and book build
        if (mdi.side() == BuySell.buy)
            aggregated.bid(mdi.price());
        else
            aggregated.ask(mdi.price());
        aggregated.transactTime(mdi.transactTime());
        if (aggregated.valid())
            out.marketDataSnapshot(aggregated);
    }
}
