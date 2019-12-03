/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;

public class NewOrderSingle extends AbstractEvent<NewOrderSingle> {

    private String clOrdID = "";

    @LongConversion(Base85LongConverter.class)
    private long symbol;

    private BuySell side;

    @LongConversion(MicroTimestampLongConverter.class)
    private long transactTime;

    private double orderQty;

    private double price;

    private OrderType ordType;

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

    public OrderType ordType() {
        return ordType;
    }

    public NewOrderSingle ordType(OrderType ordType) {
        this.ordType = ordType;
        return this;
    }
}
