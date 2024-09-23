/*
 * Copyright (c) 2016-2024 Chronicle Software Ltd
 */

package town.lost.oms.dto;

import net.openhft.chronicle.bytes.BytesIn;
import net.openhft.chronicle.bytes.BytesOut;
import net.openhft.chronicle.core.io.InvalidMarshallableException;
import net.openhft.chronicle.wire.*;
import net.openhft.chronicle.wire.converter.NanoTime;
import net.openhft.chronicle.wire.converter.ShortText;

import static town.lost.oms.dto.ValidateUtil.invalidPrice;
import static town.lost.oms.dto.ValidateUtil.invalidQuantity;

/**
 * The {@code NewOrderSingle} class represents a new single order in a trading system.
 *
 * <p>This class extends the {@link AbstractEvent} class, with the type parameter being {@link NewOrderSingle}.
 * This indicates that the event will be processed into a {@link NewOrderSingle} that will be sent to the order
 * system.
 *
 * <p>Each {@code NewOrderSingle} contains various pieces of information about the order, including:
 *
 * <ul>
 *   <li><strong>symbol</strong>: The identifier of the financial instrument.</li>
 *   <li><strong>transactTime</strong>: The transaction time in nanoseconds.</li>
 *   <li><strong>account</strong>: The account associated with the order.</li>
 *   <li><strong>orderQty</strong>: The quantity of the order.</li>
 *   <li><strong>price</strong>: The price of the order.</li>
 *   <li><strong>side</strong>: The side of the order (buy or sell).</li>
 *   <li><strong>clOrdID</strong>: The client order ID.</li>
 *   <li><strong>ordType</strong>: The type of the order (e.g., market or limit).</li>
 *   <li><strong>timeInForce</strong>: The time-in-force instruction for the order.</li>
 *   <li><strong>currency</strong>: The currency of the order.</li>
 * </ul>
 *
 * <p>The symbol is encoded using {@link ShortText} and the transaction time is in nanoseconds, both to save space.
 * The client order ID is a string that identifies the order, the side indicates whether the order is to buy or sell,
 * and the order type indicates whether the order is a market order or a limit order.
 */
public class NewOrderSingle extends AbstractEvent<NewOrderSingle> {
    private static final int MASHALLABLE_VERSION = 1;
    // Symbol of the financial instrument.
    @ShortText
    private long symbol;
    // Transaction time in nanoseconds.
    @NanoTime
    private long transactTime;
    @ShortText
    private long account;

    // Quantity of the order.
    private double orderQty;

    // Price of the order.
    private double price;

    // Side of the order (buy or sell).
    private Side side;

    // Client order ID.
    private String clOrdID = "";

    // Type of the order (market or limit).
    private OrderType ordType;

    // Time-in-force instruction for the order.
    private TimeInForce timeInForce;

    // Currency of the order.
    private Ccy currency;

    /**
     * Get the client order ID.
     *
     * @return The client order ID as a string.
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Sets the client order ID.
     *
     * @param clOrdID The client order ID to set, as a string.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
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
     * Sets the symbol of the financial instrument.
     *
     * @param symbol The symbol to set, as a long.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
     */
    public NewOrderSingle symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Get the side of the order (buy or sell).
     *
     * @return The side as a {@link Side} enum value.
     */
    public Side side() {
        return side;
    }

    /**
     * Sets the side of the order (buy or sell).
     *
     * @param side The side to set, as a {@link Side} enum value.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
     */
    public NewOrderSingle side(Side side) {
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
     * Sets the transaction time in nanoseconds.
     *
     * @param transactTime The transaction time to set, as a long.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
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
     * Sets the quantity of the order.
     *
     * @param orderQty The order quantity to set, as a double.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
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
     * Sets the price of the order.
     *
     * @param price The price to set, as a double.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
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
     * Sets the type of the order (market or limit).
     *
     * @param ordType The order type to set, as an {@link OrderType} enum value.
     * @return This {@code NewOrderSingle} instance, to facilitate method chaining.
     */
    public NewOrderSingle ordType(OrderType ordType) {
        this.ordType = ordType;
        return this;
    }

    /**
     * Retrieves the time-in-force instruction for the order.
     *
     * @return The time-in-force as a {@link TimeInForce} enum value.
     */
    public TimeInForce timeInForce() {
        return timeInForce;
    }

    /**
     * Sets the time-in-force instruction for the order.
     *
     * @param timeInForce The time-in-force to set.
     * @return This {@code NewOrderSingle} instance for method chaining.
     */
    public NewOrderSingle timeInForce(TimeInForce timeInForce) {
        this.timeInForce = timeInForce;
        return this;
    }

    /**
     * Retrieves the account associated with the order.
     *
     * @return The account as a {@code long}.
     */
    public long account() {
        return account;
    }

    /**
     * Sets the account associated with the order.
     *
     * @param account The account to set.
     * @return This {@code NewOrderSingle} instance for method chaining.
     */
    public NewOrderSingle account(long account) {
        this.account = account;
        return this;
    }

    /**
     * Retrieves the currency of the order.
     *
     * @return The currency as a {@link Ccy} enum value.
     */
    public Ccy currency() {
        return currency;
    }

    /**
     * Sets the currency of the order.
     *
     * @param currency The currency to set.
     * @return This {@code NewOrderSingle} instance for method chaining.
     */
    public NewOrderSingle currency(Ccy currency) {
        this.currency = currency;
        return this;
    }

    @Override
    public void writeMarshallable(BytesOut<?> out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.writeStopBit(MASHALLABLE_VERSION);
            out.writeLong(symbol);
            out.writeLong(transactTime);
            out.writeLong(account);
            out.writeDouble(orderQty);
            out.writeDouble(price);
            out.writeObject(Side.class, side);
            out.writeObject(OrderType.class, ordType);
            out.writeObject(String.class, clOrdID);
            out.writeObject(TimeInForce.class, timeInForce);
            out.writeObject(Ccy.class, currency);
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
                account = in.readLong();
                orderQty = in.readDouble();
                price = in.readDouble();
                side = in.readObject(Side.class);
                ordType = in.readObject(OrderType.class);
                clOrdID = in.readObject(String.class);
                timeInForce = in.readObject(TimeInForce.class);
                currency = in.readObject(Ccy.class);
            }
        }
    }

    @Override
    public void writeMarshallable(WireOut out) {
        super.writeMarshallable(out);
        if (PREGENERATED_MARSHALLABLE) {
            out.write("symbol").writeLong(ShortTextLongConverter.INSTANCE, symbol);
            out.write("transactTime").writeLong(NanoTimestampLongConverter.INSTANCE, transactTime);
            out.write("account").writeLong(ShortTextLongConverter.INSTANCE, account);
            out.write("orderQty").writeDouble(orderQty);
            out.write("price").writeDouble(price);
            out.write("side").object(Side.class, side);
            out.write("ordType").object(OrderType.class, ordType);
            out.write("clOrdID").object(String.class, clOrdID);
            out.write("timeInForce").object(TimeInForce.class, timeInForce);
            out.write("currency").object(Ccy.class, currency);
        }
    }

    @Override
    public void readMarshallable(WireIn in) {
        super.readMarshallable(in);
        if (PREGENERATED_MARSHALLABLE) {
            symbol = in.read("symbol").readLong(ShortTextLongConverter.INSTANCE);
            transactTime = in.read("transactTime").readLong(NanoTimestampLongConverter.INSTANCE);
            account = in.read("account").readLong(ShortTextLongConverter.INSTANCE);
            orderQty = in.read("orderQty").readDouble();
            price = in.read("price").readDouble();
            side = in.read("side").object(side, Side.class);
            ordType = in.read("ordType").object(OrderType.class);
            clOrdID = in.read("clOrdID").object(clOrdID, String.class);
            timeInForce = in.read("timeInForce").object(TimeInForce.class);
            currency = in.read("currency").object(Ccy.class);
        }
    }

    /**
     * Validates the fields of this {@code NewOrderSingle} event.
     *
     * @throws InvalidMarshallableException if any required field is missing or invalid.
     */
    @Override
    public void validate() throws InvalidMarshallableException {
        super.validate();
        if (symbol == 0)
            throw new InvalidMarshallableException("symbol is required");
        if (transactTime == 0)
            throw new InvalidMarshallableException("transactTime is required");
        if (account == 0)
            throw new InvalidMarshallableException("account is required");
        if (orderQty == 0)
            throw new InvalidMarshallableException("orderQty is required");
        if (invalidQuantity(orderQty))
            throw new InvalidMarshallableException("orderQty is invalid");
        if (invalidPrice(price))
            throw new InvalidMarshallableException("price is invalid");
        if (side == null)
            throw new InvalidMarshallableException("side is required");
        if (ordType == null)
            throw new InvalidMarshallableException("ordType is required");
        if (clOrdID == null || clOrdID.isEmpty())
            throw new InvalidMarshallableException("clOrdID is required");
        if (timeInForce == null)
            throw new InvalidMarshallableException("timeInForce is required");
        if (currency == null)
            throw new InvalidMarshallableException("currency is required");
    }
}
