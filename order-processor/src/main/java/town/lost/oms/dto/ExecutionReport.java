/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.NanoTimestampLongConverter;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;

/**
 * The {@code ExecutionReport} class represents the execution report of an order in a trading system.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link ExecutionReport}.
 * This indicates that the event will be processed into an {@link ExecutionReport} that represents the execution report of an order.</p>
 *
 * <p>Each {@code ExecutionReport} contains various pieces of information about the order execution,
 * including the symbol of the financial instrument, the transaction time, the quantity and price of the order,
 * the side (buy or sell), the client order ID, the order type (market or limit), the last traded price,
 * the remaining quantity, the accumulated quantity, the average price, and a text message.</p>
 *
 * <p>The symbol is encoded using Base85 and the transaction time is in nanoseconds, both to save space.
 * The client order ID is a string that identifies the order, the side indicates whether the order is to buy or sell,
 * and the order type indicates whether the order is a market order or a limit order.</p>
 */
public class ExecutionReport extends AbstractEvent<ExecutionReport> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol of the financial instrument. Encoded using Base85.
    @Base85
    private long symbol;
    // Transaction time in nanoseconds.
    @NanoTime
    private long transactTime;
    // Quantity of the order.
    private double orderQty;
    // Price of the order.
    private double price;
    // Order ID in nanoseconds.
    @NanoTime
    private long orderID;
    // Last traded price of the order.
    private double lastPx;
    // Remaining quantity of the order.
    private double leavesQty;
    // Accumulated quantity of the order.
    private double cumQty;
    // Average price of the order.
    private double avgPx;
    // Side of the order (buy or sell).
    private BuySell side;
    // Type of the order (market or limit).
    private OrderType ordType;
    // Client order ID.
    private String clOrdID = "";
    // Optional text message about the order execution.
    private String text = null;

    /**
     * Returns the client order ID.
     *
     * @return the client order ID
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Sets the client order ID and returns this ExecutionReport instance.
     *
     * @param clOrdID the client order ID to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    /**
     * Returns the symbol for the financial instrument.
     *
     * @return the symbol for the financial instrument
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Sets the symbol for the financial instrument and returns this ExecutionReport instance.
     *
     * @param symbol the symbol to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Returns the side of the market (Buy/Sell).
     *
     * @return the side of the market
     */
    public BuySell side() {
        return side;
    }

    /**
     * Sets the side of the market (Buy/Sell) and returns this ExecutionReport instance.
     *
     * @param side the side of the market to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport side(BuySell side) {
        this.side = side;
        return this;
    }
    /**
     * Returns the transaction time.
     *
     * @return the transaction time
     */
    public long transactTime() {
        return transactTime;
    }
    /**
     * Sets the transaction time and returns this ExecutionReport instance.
     *
     * @param transactTime the transaction time to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }
    /**
     * Returns the order quantity.
     *
     * @return the order quantity
     */
    public double orderQty() {
        return orderQty;
    }
    /**
     * Sets the order quantity and returns this ExecutionReport instance.
     *
     * @param orderQty the order quantity to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport orderQty(double orderQty) {
        this.orderQty = orderQty;
        return this;
    }
    /**
     * Returns the price.
     *
     * @return the price
     */
    public double price() {
        return price;
    }
    /**
     * Sets the price and returns this ExecutionReport instance.
     *
     * @param price the price to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport price(double price) {
        this.price = price;
        return this;
    }
    /**
     * Returns the order ID.
     *
     * @return the order ID
     */
    public long orderID() {
        return orderID;
    }
    /**
     * Sets the order ID and returns this ExecutionReport instance.
     *
     * @param orderID the order ID to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport orderID(long orderID) {
        this.orderID = orderID;
        return this;
    }
    /**
     * Returns the order type.
     *
     * @return the order type
     */
    public OrderType ordType() {
        return ordType;
    }
    /**
     * Sets the order type and returns this ExecutionReport instance.
     *
     * @param ordType the order type to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport ordType(OrderType ordType) {
        this.ordType = ordType;
        return this;
    }
    /**
     * Returns the last price.
     *
     * @return the last price
     */
    public double lastPx() {
        return lastPx;
    }
    /**
     * Sets the last price and returns this ExecutionReport instance.
     *
     * @param lastPx the last price to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport lastPx(double lastPx) {
        this.lastPx = lastPx;
        return this;
    }
    /**
     * Returns the leaves quantity.
     *
     * @return the leaves quantity
     */
    public double leavesQty() {
        return leavesQty;
    }
    /**
     * Sets the leaves quantity and returns this ExecutionReport instance.
     *
     * @param leavesQty the leaves quantity to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport leavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
        return this;
    }
    /**
     * Returns the cumulative quantity.
     *
     * @return the cumulative quantity
     */
    public double cumQty() {
        return cumQty;
    }
    /**
     * Sets the cumulative quantity and returns this ExecutionReport instance.
     *
     * @param cumQty the cumulative quantity to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport cumQty(double cumQty) {
        this.cumQty = cumQty;
        return this;
    }
    /**
     * Returns the average price.
     *
     * @return the average price
     */
    public double avgPx() {
        return avgPx;
    }
    /**
     * Sets the average price and returns this ExecutionReport instance.
     *
     * @param avgPx the average price to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport avgPx(double avgPx) {
        this.avgPx = avgPx;
        return this;
    }
    /**
     * Returns the text of the report.
     *
     * @return the text of the report
     */
    public String text() {
        return text;
    }
    /**
     * Sets the text of the report and returns this ExecutionReport instance.
     *
     * @param text the text to set
     * @return this ExecutionReport instance
     */
    public ExecutionReport text(String text) {
        this.text = text;
        return this;
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
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
    }

    @Override
    public void readMarshallable(BytesIn<?> in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
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
                side = in.readObject(BuySell.class);
                ordType = in.readObject(OrderType.class);
                clOrdID = in.readObject(String.class);
                text = in.readObject(String.class);
            } else {
                throw new IllegalStateException("Unknown version " + version);
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
            out.write("orderID").writeLong(NanoTimestampLongConverter.INSTANCE, orderID);
            out.write("lastPx").writeDouble(lastPx);
            out.write("leavesQty").writeDouble(leavesQty);
            out.write("cumQty").writeDouble(cumQty);
            out.write("avgPx").writeDouble(avgPx);
            out.write("side").object(BuySell.class, side);
            out.write("ordType").object(OrderType.class, ordType);
            out.write("clOrdID").object(String.class, clOrdID);
            out.write("text").object(String.class, text);
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
            orderID = in.read("orderID").readLong(NanoTimestampLongConverter.INSTANCE);
            lastPx = in.read("lastPx").readDouble();
            leavesQty = in.read("leavesQty").readDouble();
            cumQty = in.read("cumQty").readDouble();
            avgPx = in.read("avgPx").readDouble();
            side = in.read("side").object(side, BuySell.class);
            ordType = in.read("ordType").object(ordType, OrderType.class);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
            text = in.read("text").object(text, String.class);
        }
    }
}
