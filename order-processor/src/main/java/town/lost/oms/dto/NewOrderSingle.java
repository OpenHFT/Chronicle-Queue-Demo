/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.wire.*;

public class NewOrderSingle extends AbstractEvent<NewOrderSingle> {
    private static final int MASHALLABLE_VERSION = 1;
    @LongConversion(Base85LongConverter.class)
    private long symbol;
    @LongConversion(NanoTimestampLongConverter.class)
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
    public void writeMarshallable(BytesOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeLong(symbol);
            out.writeLong(transactTime);
            out.writeDouble(orderQty);
            out.writeDouble(price);
            out.writeObject(BuySell.class, side);
            out.writeObject(OrderType.class, ordType);
            out.writeObject(String.class, clOrdID);
        }
    }

    @Override
    public void readMarshallable(BytesIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            int version = (int) in.readStopBit();
            if (version == MASHALLABLE_VERSION) {
                symbol = in.readLong();
                transactTime = in.readLong();
                orderQty = in.readDouble();
                price = in.readDouble();
                side = (BuySell) in.readObject(BuySell.class);
                ordType = (OrderType) in.readObject(OrderType.class);
                clOrdID = (String) in.readObject(String.class);
            }
        }
    }

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(Base85LongConverter.INSTANCE, symbol);
            out.write("transactTime").writeLong(NanoTimestampLongConverter.INSTANCE, transactTime);
            out.write("orderQty").writeDouble(orderQty);
            out.write("price").writeDouble(price);
            out.write("side").object(BuySell.class, side);
            out.write("ordType").object(OrderType.class, ordType);
            out.write("clOrdID").object(String.class, clOrdID);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(Base85LongConverter.INSTANCE);
            transactTime = in.read("transactTime").readLong(NanoTimestampLongConverter.INSTANCE);
            orderQty = in.read("orderQty").readDouble();
            price = in.read("price").readDouble();
            side = in.read("side").object(side, BuySell.class);
            ordType = in.read("ordType").object(ordType, OrderType.class);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
        }
    }
}
