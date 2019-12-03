/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.*;

public class NewOrderSingle extends AbstractEvent<NewOrderSingle> {

    @LongConversion(Base85LongConverter.class)
    private long symbol;

    @LongConversion(MicroTimestampLongConverter.class)
    private long transactTime;

    private double orderQty;

    private double price;

    private BuySell side;

    private OrderType ordType;

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

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable0(out);
        out.write("symbol").writeLong(symbol);
        out.write("transactTime").writeLong(transactTime);
        out.write("orderQty").writeDouble(orderQty);
        out.write("price").writeDouble(price);
        out.write("clOrdID").object(String.class, clOrdID);
        out.write("side").object(BuySell.class, side);
        out.write("ordType").object(OrderType.class, ordType);
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable0(in);
        symbol = in.read("symbol").readLong();
        transactTime = in.read("transactTime").readLong();
        orderQty = in.read("orderQty").readDouble();
        price = in.read("price").readDouble();
        clOrdID = in.read("clOrdID").object(clOrdID, String.class);
        side = in.read("side").object(side, BuySell.class);
        ordType = in.read("ordType").object(ordType, OrderType.class);
    }
}
