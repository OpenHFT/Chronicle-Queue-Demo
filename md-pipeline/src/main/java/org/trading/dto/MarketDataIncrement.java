/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */

package org.trading.dto;

import net.openhft.chronicle.wire.Base85LongConverter;
import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;

/**
 * This is a MarketDataIncrement class extending SelfDescribingMarshallable.
 * It represents an incremental update to market data and encapsulates information
 * such as symbol, transaction time, quantity, rate, and side of the transaction.
 */
public class MarketDataIncrement extends SelfDescribingMarshallable {

    @Base85
    // The symbol representing the asset in Base85 encoding
    private long symbol;

    @NanoTime
    // The time of the transaction in nanoseconds
    private long transactTime;

    // The quantity of the asset in the transaction
    private double qty;

    // The rate of the asset in the transaction
    private double rate;

    // The side of the transaction, i.e., Buy or Sell
    private BuySell side;

    /**
     * Retrieves the symbol for this market data increment.
     *
     * @return A long value representing the symbol
     */
    public long symbol() {
        return symbol;
    }

    /**
     * Sets the symbol for this market data increment.
     *
     * @param symbol A long value representing the symbol
     * @return This instance of MarketDataIncrement
     */
    public MarketDataIncrement symbol(long symbol) {
        this.symbol = symbol;
        return this;
    }

    /**
     * Sets the symbol for this market data increment using a String.
     *
     * @param s A string representing the symbol
     * @return This instance of MarketDataIncrement
     */
    public MarketDataIncrement symbol(String s) {
        return symbol(Base85LongConverter.INSTANCE.parse(s));
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
     * @return This instance of MarketDataIncrement
     */
    public MarketDataIncrement side(BuySell side) {
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
     * @return This instance of MarketDataIncrement
     */
    public MarketDataIncrement transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    /**
     * Retrieves the order quantity.
     *
     * @return A double value representing the order quantity
     */
    public double orderQty() {
        return qty;
    }

    /**
     * Sets the order quantity.
     *
     * @param orderQty A double value representing the order quantity
     * @return This instance of MarketDataIncrement
     */
    public MarketDataIncrement orderQty(double orderQty) {
        this.qty = orderQty;
        return this;
    }

    /**
     * Retrieves the price.
     *
     * @return A double value representing the price
     */
    public double price() {
        return rate;
    }

    /**
     * Sets the price.
     *
     * @param price A double value representing the price
     * @return This instance of MarketDataIncrement
     */
    public MarketDataIncrement price(double price) {
        this.rate = price;
        return this;
    }
}
