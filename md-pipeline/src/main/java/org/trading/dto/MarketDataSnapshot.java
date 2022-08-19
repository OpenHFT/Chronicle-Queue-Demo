/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class MarketDataSnapshot extends SelfDescribingMarshallable {
    @LongConversion(Base85LongConverter.class)
    private long symbol;
    @LongConversion(MicroTimestampLongConverter.class)
    private long transactTime;
    private double bid;
    private double ask;

    public MarketDataSnapshot() {
    }

    public MarketDataSnapshot(long symbol) {
        this.symbol = symbol;
        this.bid = Double.NaN;
        this.ask = Double.NaN;
    }

    public long symbol() {
        return symbol;
    }

    public MarketDataSnapshot symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    public long transactTime() {
        return transactTime;
    }

    public MarketDataSnapshot transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    public double bid() {
        return bid;
    }

    public MarketDataSnapshot bid(double bid) {
        this.bid = bid;
        return this;
    }

    public double ask() {
        return ask;
    }

    public MarketDataSnapshot ask(double ask) {
        this.ask = ask;
        return this;
    }

    public double spread() {
        return ask() - bid();
    }

    public boolean valid() {
        return !(Double.isNaN(ask) || Double.isNaN(bid));
    }
}
