/*
 * Copyright 2016-2025 chronicle.software
 */
package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.ShortTextLongConverter;
import net.openhft.chronicle.wire.NanoTimestampLongConverter;
import net.openhft.chronicle.wire.WireIn;
import net.openhft.chronicle.wire.WireOut;
import net.openhft.chronicle.wire.converter.NanoTime;
import net.openhft.chronicle.wire.converter.ShortText;

import static town.lost.oms.dto.ValidateUtil.*;

/**
 * The {@code ExecutionReport} class represents the execution report of an order in a trading system.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link ExecutionReport}.
 * This indicates that the event will be processed into an {@link ExecutionReport} that represents the execution report of an order.
 *
 * <p>Each {@code ExecutionReport} contains various pieces of information about the order execution, including:
 *
 * The {@code ExecutionReport} class corresponds to a FIX 4.2 "Execution Report" (MsgType=35=8).
 * Used for order status updates, including partial fills, full fills, cancellations, etc.
 * <p>
 * Typical FIX tag mappings:
 * <ul>
 *   <li>{@code symbol} (FIX 55) - Security identifier.</li>
 *   <li>{@code side} (FIX 54) - Side of the order.</li>
 *   <li>{@code orderQty} (FIX 38) - Original quantity.</li>
 *   <li>{@code price} (FIX 44) - Limit price (if applicable).</li>
 *   <li>{@code orderID} (FIX 37) - Unique identifier assigned by the broker/exchange.</li>
 *   <li>{@code clOrdID} (FIX 11) - Client’s original order ID (if relevant).</li>
 *   <li>{@code lastPx} (FIX 31) - Price of the last fill.</li>
 *   <li>{@code leavesQty} (FIX 151) - Remaining quantity not yet filled.</li>
 *   <li>{@code cumQty} (FIX 14) - Total quantity filled so far.</li>
 *   <li>{@code avgPx} (FIX 6) - Average fill price across partial executions.</li>
 *   <li>{@code text} (FIX 58) - Free-form text for clarifications or rejections.</li>
 *   <li>{@code transactTime} (FIX 60) - Time of the transaction/fill.</li>
 *   <li>{@code sendingTime} (FIX 52, optional) - Time at which the ExecutionReport is sent.</li>
 *   <li>{@code ordType} (FIX 40) - The original order type (e.g., MARKET, LIMIT).</li>
 *   <li>{@code sender}/{@code target} - Analogous to SenderCompID (49)/TargetCompID (56).</li>
 * </ul>
 *
 * <p>The {@code symbol} field is encoded using {@link ShortTextLongConverter}, and {@code transactTime} and {@code orderID}
 * use {@link NanoTimestampLongConverter} to save space.
 */
public class ExecutionReport extends AbstractEvent<ExecutionReport> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol of the financial instrument.
    @ShortText
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
    private Side side;

    // Type of the order (market or limit).
    private OrderType ordType;

    // Client order ID.
    private String clOrdID = "";

    // Optional text message about the order execution.
    private String text = null;

    /**
     * Retrieves the client order ID.
     *
     * @return the client order ID as a {@code String}
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Sets the client order ID.
     *
     * @param clOrdID the client order ID to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    /**
     * Retrieves the symbol of the financial instrument.
     *
     * @return the symbol as a {@code long}
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Sets the symbol of the financial instrument.
     *
     * @param symbol the symbol to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Retrieves the side of the order (buy or sell).
     *
     * @return the side as a {@link Side} enum value
     */
    public Side side() {
        return side;
    }

    /**
     * Sets the side of the order (buy or sell).
     *
     * @param side the side to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport side(Side side) {
        this.side = side;
        return this;
    }

    /**
     * Retrieves the transaction time in nanoseconds.
     *
     * @return the transaction time as a {@code long}
     */
    public long transactTime() {
        return transactTime;
    }

    /**
     * Sets the transaction time in nanoseconds.
     *
     * @param transactTime the transaction time to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    /**
     * Retrieves the quantity of the order.
     *
     * @return the order quantity as a {@code double}
     */
    public double orderQty() {
        return orderQty;
    }

    /**
     * Sets the quantity of the order.
     *
     * @param orderQty the order quantity to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport orderQty(double orderQty) {
        this.orderQty = orderQty;
        return this;
    }

    /**
     * Retrieves the price of the order.
     *
     * @return the price as a {@code double}
     */
    public double price() {
        return price;
    }

    /**
     * Sets the price of the order.
     *
     * @param price the price to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport price(double price) {
        this.price = price;
        return this;
    }

    /**
     * Retrieves the order ID.
     *
     * @return the order ID as a {@code long}
     */
    public long orderID() {
        return orderID;
    }

    /**
     * Sets the order ID.
     *
     * @param orderID the order ID to set
     * @return this {@code ExecutionReport} instance for method chaining
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
     * Retrieves the last traded price of the order.
     *
     * @return the last traded price as a {@code double}
     */
    public double lastPx() {
        return lastPx;
    }

    /**
     * Sets the last traded price of the order.
     *
     * @param lastPx the last price to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport lastPx(double lastPx) {
        this.lastPx = lastPx;
        return this;
    }

    /**
     * Retrieves the remaining quantity of the order.
     *
     * @return the leaves quantity as a {@code double}
     */
    public double leavesQty() {
        return leavesQty;
    }

    /**
     * Sets the remaining quantity of the order.
     *
     * @param leavesQty the leaves quantity to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport leavesQty(double leavesQty) {
        this.leavesQty = leavesQty;
        return this;
    }

    /**
     * Retrieves the accumulated quantity of the order.
     *
     * @return the cumulative quantity as a {@code double}
     */
    public double cumQty() {
        return cumQty;
    }

    /**
     * Sets the accumulated quantity of the order.
     *
     * @param cumQty the cumulative quantity to set
     * @return this {@code ExecutionReport} instance for method chaining
     */
    public ExecutionReport cumQty(double cumQty) {
        this.cumQty = cumQty;
        return this;
    }

    /**
     * Retrieves the average price of the order.
     *
     * @return the average price as a {@code double}
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
     * Retrieves the optional text message about the order execution.
     *
     * @return the text message as a {@code String}
     */
    public String text() {
        return text;
    }

    /**
     * Sets the text of the report and returns this ExecutionReport instance.
     *
     * @param text the text message to set
     * @return this {@code ExecutionReport} instance for method chaining
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
            out.writeObject(Side.class, side);
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
                side = in.readObject(Side.class);
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
            out.write("symbol").writeLong(ShortTextLongConverter.INSTANCE, symbol);
            out.write("transactTime").writeLong(NanoTimestampLongConverter.INSTANCE, transactTime);
            out.write("orderQty").writeDouble(orderQty);
            out.write("price").writeDouble(price);
            out.write("orderID").writeLong(NanoTimestampLongConverter.INSTANCE, orderID);
            out.write("lastPx").writeDouble(lastPx);
            out.write("leavesQty").writeDouble(leavesQty);
            out.write("cumQty").writeDouble(cumQty);
            out.write("avgPx").writeDouble(avgPx);
            out.write("side").object(Side.class, side);
            out.write("ordType").object(OrderType.class, ordType);
            out.write("clOrdID").object(String.class, clOrdID);
            out.write("text").object(String.class, text);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(ShortTextLongConverter.INSTANCE);
            transactTime = in.read("transactTime").readLong(NanoTimestampLongConverter.INSTANCE);
            orderQty = in.read("orderQty").readDouble();
            price = in.read("price").readDouble();
            orderID = in.read("orderID").readLong(NanoTimestampLongConverter.INSTANCE);
            lastPx = in.read("lastPx").readDouble();
            leavesQty = in.read("leavesQty").readDouble();
            cumQty = in.read("cumQty").readDouble();
            avgPx = in.read("avgPx").readDouble();
            side = in.read("side").object(side, Side.class);
            ordType = in.read("ordType").object(ordType, OrderType.class);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
            text = in.read("text").object(text, String.class);
        }
    }

    /**
     * Validates the fields of this {@code ExecutionReport} event.
     *
     * @throws InvalidMarshallableException if any required field is missing or invalid
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate();
        if (symbol == 0)
            throw new InvalidMarshallableException("symbol is required");
        if (transactTime == 0)
            throw new InvalidMarshallableException("transactTime is required");
        if (invalidQuantity(orderQty))
            throw new InvalidMarshallableException("orderQty is invalid");
        if (invalidPrice(price))
            throw new InvalidMarshallableException("price is invalid");
        if (orderID == 0)
            throw new InvalidMarshallableException("orderID is required");
        if (lastPx != 0 && invalidPrice(lastPx))
            throw new InvalidMarshallableException("lastPx is invalid");
        if (invalidQuantity(leavesQty))
            throw new InvalidMarshallableException("leavesQty is invalid");
        if (invalidQuantity(cumQty))
            throw new InvalidMarshallableException("cumQty is invalid");
        if (avgPx != 0 && invalidPrice(avgPx))
            throw new InvalidMarshallableException("avgPx is invalid");
        if (side == null)
            throw new InvalidMarshallableException("side is required");
        if (ordType == null)
            throw new InvalidMarshallableException("ordType is required");
        if (clOrdID == null || clOrdID.isEmpty())
            throw new InvalidMarshallableException("clOrdID is required");
    }
}
