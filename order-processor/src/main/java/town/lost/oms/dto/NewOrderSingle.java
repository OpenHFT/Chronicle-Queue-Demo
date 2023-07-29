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
 * The {@code NewOrderSingle} class represents a new single order in a trading system.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link NewOrderSingle}.
 * This indicates that the event will be processed into a {@link NewOrderSingle} that will be sent to the order
 * system.</p>
 *
 * <p>Each {@code NewOrderSingle} contains various pieces of information about the order, including the symbol of the
 * financial instrument, the transaction time, the quantity and price of the order, the side (buy or sell),
 * the client order ID, and the type of the order (market or limit).</p>
 *
 * <p>The symbol is encoded using Base85 and the transaction time is in nanoseconds, both to save space.
 * The client order ID is a string that identifies the order, the side indicates whether the order is to buy or sell,
 * and the order type indicates whether the order is a market order or a limit order.</p>
 */
public class NewOrderSingle extends AbstractEvent<NewOrderSingle> {
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
    // Side of the order (buy or sell).
    private BuySell side;
    // Client order ID.
    private String clOrdID = "";
    // Type of the order (market or limit).
    private OrderType ordType;

    /**
     * Get the client order ID.
     *
     * @return The client order ID as a string.
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Set the client order ID.
     *
     * @param clOrdID The client order ID to set, as a string.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    /**
     * Get the symbol of the financial instrument.
     *
     * @return The symbol as a long.
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Set the symbol of the financial instrument.
     *
     * @param symbol The symbol to set, as a long.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Get the side of the order (buy or sell).
     *
     * @return The side as a {@link BuySell} enum value.
     */
    public BuySell side() {
        return side;
    }

    /**
     * Set the side of the order (buy or sell).
     *
     * @param side The side to set, as a {@link BuySell} enum value.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle side(BuySell side) {
        this.side = side;
        return this;
    }

    /**
     * Get the transaction time in nanoseconds.
     *
     * @return The transaction time as a long.
     */
    public long transactTime() {
        return transactTime;
    }

    /**
     * Set the transaction time in nanoseconds.
     *
     * @param transactTime The transaction time to set, as a long.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    /**
     * Get the quantity of the order.
     *
     * @return The order quantity as a double.
     */
    public double orderQty() {
        return orderQty;
    }

    /**
     * Set the quantity of the order.
     *
     * @param orderQty The order quantity to set, as a double.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle orderQty(double orderQty) {
        this.orderQty = orderQty;
        return this;
    }

    /**
     * Get the price of the order.
     *
     * @return The price as a double.
     */
    public double price() {
        return price;
    }

    /**
     * Set the price of the order.
     *
     * @param price The price to set, as a double.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle price(double price) {
        this.price = price;
        return this;
    }

    /**
     * Get the type of the order (market or limit).
     *
     * @return The order type as an {@link OrderType} enum value.
     */
    public OrderType ordType() {
        return ordType;
    }

    /**
     * Set the type of the order (market or limit).
     *
     * @param ordType The order type to set, as an {@link OrderType} enum value.
     * @return This NewOrderSingle instance, to facilitate method chaining.
     */
    public NewOrderSingle ordType(OrderType ordType) {
        this.ordType = ordType;
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
            out.writeObject(BuySell.class, side);
            out.writeObject(OrderType.class, ordType);
            out.writeObject(String.class, clOrdID);
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
                side = in.readObject(BuySell.class);
                ordType = in.readObject(OrderType.class);
                clOrdID = in.readObject(String.class);
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
