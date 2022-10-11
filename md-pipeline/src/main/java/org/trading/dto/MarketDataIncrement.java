/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;

public class MarketDataIncrement extends SelfDescribingMarshallable {
    @Base85
    private long symbol;
    @NanoTime
    private long transactTime;
    private double qty;
    private double rate;
    private BuySell side;

    public long symbol() {
        return symbol;
    }

    public MarketDataIncrement symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    public MarketDataIncrement symbol(String s) {
        return symbol(Base85LongConverter.INSTANCE.parse(s));
    }

    public BuySell side() {
        return side;
    }

    public MarketDataIncrement side(BuySell side) {
        this.side = side;
        return this;
    }

    public long transactTime() {
        return transactTime;
    }

    public MarketDataIncrement transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    public double orderQty() {
        return qty;
    }

    public MarketDataIncrement orderQty(double orderQty) {
        this.qty = orderQty;
        return this;
    }

    public double price() {
        return rate;
    }

    public MarketDataIncrement price(double price) {
        this.rate = price;
        return this;
    }
}
