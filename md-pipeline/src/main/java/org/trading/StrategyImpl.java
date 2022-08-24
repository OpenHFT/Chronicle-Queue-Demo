/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading;

import org.trading.api.AggregatorOut;
import org.trading.api.OMSIn;
import org.trading.dto.BuySell;
import org.trading.dto.MarketDataSnapshot;
import org.trading.dto.NewOrderSingle;

public class StrategyImpl implements AggregatorOut {
    private final OMSIn out;
    private final NewOrderSingle nos;
    private int clordId;

    public StrategyImpl(OMSIn out) {
        this.out = out;
        this.nos = new NewOrderSingle();
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

    public static void main(String[] args) {
        Runner.run("agg-out", "strat-out", OMSIn.class, StrategyImpl::new);
    }
}
