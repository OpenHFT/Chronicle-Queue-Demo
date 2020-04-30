/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.wire.Base32LongConverter;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.LongConversion;
import net.openhft.chronicle.wire.MicroTimestampLongConverter;

public class ExecutionReport extends AbstractEvent<ExecutionReport> {
    @LongConversion(Base85LongConverter.class)
    private long symbol;
    @LongConversion(MicroTimestampLongConverter.class)
    private long transactTime;
    private double orderQty;
    private double price;
    @LongConversion(Base32LongConverter.class)
    private long orderID;
    private double lastPx;
    private double leavesQty;
    private double cumQty;
    private double avgPx;
    private BuySell side;
    private OrderType ordType;
    private String clOrdID = "";
    private String text = null;

    public String clOrdID() {
        return clOrdID;
    }

    public ExecutionReport clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    public long symbol() {
        return symbol;
    }

    public ExecutionReport symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    public BuySell side() {
        return side;
    }

    public ExecutionReport side(BuySell side) {
        this.side = side;
        return this;
    }

    public long transactTime() {
        return transactTime;
    }

    public ExecutionReport transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    public double orderQty() {
        return orderQty;
    }

    public ExecutionReport orderQty(double orderQty) {
        this.orderQty = orderQty;
        return this;
    }

    public double price() {
        return price;
    }

    public ExecutionReport price(double price) {
        this.price = price;
        return this;
    }

    public long orderID() {
        return orderID;
    }

    public ExecutionReport orderID(long orderID) {
        this.orderID = orderID;
        return this;
    }

    public OrderType ordType() {
        return ordType;
    }

    public ExecutionReport ordType(OrderType ordType) {
        this.ordType = ordType;
        return this;
    }

    public double lastPx() {
        return lastPx;
    }

    public ExecutionReport lastPx(double lastPx) {
        this.lastPx = lastPx;
        return this;
    }

    public double leavesQty() {
        return leavesQty;
    }

    public ExecutionReport leavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
        return this;
    }

    public double cumQty() {
        return cumQty;
    }

    public ExecutionReport cumQty(double cumQty) {
        this.cumQty = cumQty;
        return this;
    }

    public double avgPx() {
        return avgPx;
    }

    public ExecutionReport avgPx(double avgPx) {
        this.avgPx = avgPx;
        return this;
    }

    public String text() {
        return text;
    }

    public ExecutionReport text(String text) {
        this.text = text;
        return this;
    }

    /*
    private static final int MASHALLABLE_VERSION = 1;

    @Override
    public void writeMarshallable(BytesOut out) {
        super.writeMarshallable(out);
        out.writeStopBit(MASHALLABLE_VERSION);
        out.writeLong(symbol);
        out.writeLong(transactTime);
        out.writeDouble(orderQty);
        out.writeDouble(price);
        out.writeLong(orderID);
        out.writeDouble(lastPx);
        out.writeDouble(leavesQty);
        out.writeDouble(cumQty);
        out.writeDouble(avgPx);
        out.writeObject(BuySell.class, side);
        out.writeObject(OrderType.class, ordType);
        out.writeObject(String.class, clOrdID);
        out.writeObject(String.class, text);
    }

    @Override
    public void readMarshallable(BytesIn in) {
        super.readMarshallable(in);
        int version = (int) in.readStopBit();
        if (version == MASHALLABLE_VERSION) {
            symbol = in.readLong();
            transactTime = in.readLong();
            orderQty = in.readDouble();
            price = in.readDouble();
            orderID = in.readLong();
            lastPx = in.readDouble();
            leavesQty = in.readDouble();
            cumQty = in.readDouble();
            avgPx = in.readDouble();
            side = (BuySell) in.readObject(BuySell.class);
            ordType = (OrderType) in.readObject(OrderType.class);
            clOrdID = (String) in.readObject(String.class);
            text = (String) in.readObject(String.class);
        } else {
            throw new IllegalStateException("Unknown version " + version);
        }
    }

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        out.write("symbol").writeLong(Base85LongConverter.INSTANCE, symbol);
        out.write("transactTime").writeLong(MicroTimestampLongConverter.INSTANCE, transactTime);
        out.write("orderQty").writeDouble(orderQty);
        out.write("price").writeDouble(price);
        out.write("orderID").writeLong(Base32LongConverter.INSTANCE, orderID);
        out.write("lastPx").writeDouble(lastPx);
        out.write("leavesQty").writeDouble(leavesQty);
        out.write("cumQty").writeDouble(cumQty);
        out.write("avgPx").writeDouble(avgPx);
        out.write("side").object(BuySell.class, side);
        out.write("ordType").object(OrderType.class, ordType);
        out.write("clOrdID").object(String.class, clOrdID);
        out.write("text").object(String.class, text);
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        symbol = in.read("symbol").readLong(Base85LongConverter.INSTANCE);
        transactTime = in.read("transactTime").readLong(MicroTimestampLongConverter.INSTANCE);
        orderQty = in.read("orderQty").readDouble();
        price = in.read("price").readDouble();
        orderID = in.read("orderID").readLong(Base32LongConverter.INSTANCE);
        lastPx = in.read("lastPx").readDouble();
        leavesQty = in.read("leavesQty").readDouble();
        cumQty = in.read("cumQty").readDouble();
        avgPx = in.read("avgPx").readDouble();
        side = in.read("side").object(side, BuySell.class);
        ordType = in.read("ordType").object(ordType, OrderType.class);
        clOrdID = in.read("clOrdID").object(clOrdID, String.class);
        text = in.read("text").object(text, String.class);
    }
    */
}
