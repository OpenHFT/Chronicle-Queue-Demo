/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;

/**
 * This is a NewOrderSingle class extending SelfDescribingMarshallable.
 * It represents a new order in the trading system, encapsulating details
 * such as the symbol, transaction time, order quantity, price, side of the transaction, and client order ID.
 */
public class NewOrderSingle extends SelfDescribingMarshallable {
    @Base85
    private long symbol;
    @NanoTime
    // The time of the transaction in nanoseconds
    private long transactTime;

    // The quantity of the order
    private double orderQty;

    // The price of the order
    private double price;

    // The side of the transaction, i.e., Buy or Sell
    private BuySell side;

    // The client order ID
    private String clOrdID = "";

    /**
     * Retrieves the client order ID.
     *
     * @return A String representing the client order ID
     */
    public String clOrdID() {
        return clOrdID;
    }

    /**
     * Sets the client order ID.
     *
     * @param clOrdID A String representing the client order ID
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle clOrdID(String clOrdID) {
        this.clOrdID = clOrdID;
        return this;
    }

    /**
     * Retrieves the symbol.
     *
     * @return A long value representing the symbol
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Sets the symbol.
     *
     * @param symbol A long value representing the symbol
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Sets the symbol using a CharSequence.
     *
     * @param symbol A CharSequence representing the symbol
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle symbol(CharSequence symbol) {
        this.symbol = Base85LongConverter.INSTANCE.parse(symbol);
        return this;
    }

    /**
     * Retrieves the side of the transaction.
     *
     * @return The BuySell value representing the side of the transaction
     */
    public BuySell side() {
        return side;
    }

    /**
     * Sets the side of the transaction.
     *
     * @param side The BuySell value representing the side of the transaction
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle side(BuySell side) {
        this.side = side;
        return this;
    }

    /**
     * Retrieves the transaction time.
     *
     * @return A long value representing the transaction time
     */
    public long transactTime() {
        return transactTime;
    }

    /**
     * Sets the transaction time.
     *
     * @param transactTime A long value representing the transaction time
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    /**
     * Retrieves the order quantity.
     *
     * @return A double value representing the order quantity
     */
    public double orderQty() {
        return orderQty;
    }

    /**
     * Sets the order quantity.
     *
     * @param orderQty A double value representing the order quantity
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle orderQty(double orderQty) {
        this.orderQty = orderQty;
        return this;
    }

    /**
     * Retrieves the price.
     *
     * @return A double value representing the price
     */
    public double price() {
        return price;
    }

    /**
     * Sets the price.
     *
     * @param price A double value representing the price
     * @return This instance of NewOrderSingle
     */
    public NewOrderSingle price(double price) {
        this.price = price;
        return this;
    }
}
