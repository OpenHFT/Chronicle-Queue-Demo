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

public class AggregatorImpl implements AggregatorIn {
    private final Map<Long, MarketDataSnapshot> md = new HashMap<>();
    private final AggregatorOut out;

    public AggregatorImpl(AggregatorOut out) {
        this.out = out;
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

    public static void main(String[] args) {
        Runner.run("agg-in", "agg-out", AggregatorOut.class, AggregatorImpl::new);
    }
}
