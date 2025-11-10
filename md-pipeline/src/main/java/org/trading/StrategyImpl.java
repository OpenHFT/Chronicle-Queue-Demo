//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/*
 * Copyright 2016-2025 chronicle.software
 */

package org.trading;

import org.trading.api.AggregatorOut;
import org.trading.api.OMSIn;
import org.trading.dto.BuySell;
import org.trading.dto.MarketDataSnapshot;
import org.trading.dto.NewOrderSingle;

/**
 * This is the StrategyImpl class, implementing the {@link AggregatorOut} interface.
 * It encapsulates the strategy logic and communicates with the Order Management System (OMS) input interface.
 */
public class StrategyImpl implements AggregatorOut {
    private final OMSIn out; // The output interface for OMS
    private final NewOrderSingle nos; // Object representing a new order
    private int clordId; // The client order ID

    /**
     * Constructor for the StrategyImpl class.
     * It initialises the output interface for OMS and a new order object.
     *
     * @param out An instance of {@link OMSIn} to be used for communication with the OMS
     */
    public StrategyImpl(OMSIn out) {
        this.out = out;
        this.nos = new NewOrderSingle();
    }

    /**
     * Main method to start the Strategy implementation.
     * It calls the {@link Runner} class to run the microservice, listening and outputting to specific queues.
     *
     * @param args Command-line arguments (unused)
     */
    public static void main(String[] args) {
        Runner.run("agg-out", "strat-out", OMSIn.class, StrategyImpl::new);
    }

    @Override
    public void marketDataSnapshot(MarketDataSnapshot mds) {
        if (mds.spread() < 1)
            out.newOrderSingle(sellAtAsk(mds));
    }

    private NewOrderSingle sellAtAsk(MarketDataSnapshot mds) {
        nos.clOrdID(Integer.toString(clordId++))
                .transactTime(mds.transactTime())
                .symbol(mds.symbol())
                .orderQty(10_000)
                .price(mds.ask())
                .side(BuySell.sell);
        return nos;
    }
}
