/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;

public class NewOrderSingle extends SelfDescribingMarshallable {
    @LongConversion(Base85LongConverter.class)
    private long symbol;
    @LongConversion(MicroTimestampLongConverter.class)
    private long transactTime;
    private double orderQty;
    private double price;
    private BuySell side;
    private String clOrdID = "";

    public String clOrdID() {
        return clOrdID;
    }

    public NewOrderSingle clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    public long symbol() {
        return symbol;
    }

    public NewOrderSingle symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    public NewOrderSingle symbol(CharSequence symbol) {
        this.symbol = Base85LongConverter.INSTANCE.parse(symbol);
        return this;
    }

    public BuySell side() {
        return side;
    }

    public NewOrderSingle side(BuySell side) {
        this.side = side;
        return this;
    }

    public long transactTime() {
        return transactTime;
    }

    public NewOrderSingle transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    public double orderQty() {
        return orderQty;
    }

    public NewOrderSingle orderQty(double orderQty) {
        this.orderQty = orderQty;
        return this;
    }

    public double price() {
        return price;
    }

    public NewOrderSingle price(double price) {
        this.price = price;
        return this;
    }
}
