/*
 * Copyright 2016-2025 chronicle.software
 */
package org.trading.dto;

import net.openhft.chronicle.wire.SelfDescribingMarshallable;
import net.openhft.chronicle.wire.converter.Base85;
import net.openhft.chronicle.wire.converter.NanoTime;

/**
 * This is a MarketDataSnapshot class extending SelfDescribingMarshallable.
 * It represents a snapshot of market data for a given symbol, encapsulating information
 * such as the symbol, transaction time, bid and ask prices.
 */
public class MarketDataSnapshot extends SelfDescribingMarshallable {
    @Base85
    private long symbol;
    @NanoTime
    // The time of the transaction in nanoseconds
    private long transactTime;

    // The bid price for the asset
    private double bid;

    // The ask price for the asset
    private double ask;

    /**
     * Default constructor for MarketDataSnapshot.
     */
    public MarketDataSnapshot() {
    }

    /**
     * Constructs a MarketDataSnapshot for a given symbol, initializing bid and ask to NaN.
     *
     * @param symbol A long value representing the symbol
     */
    public MarketDataSnapshot(long symbol) {
        this.symbol = symbol;
        this.bid = Double.NaN;
        this.ask = Double.NaN;
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
     * @return This instance of MarketDataSnapshot
     */
    public MarketDataSnapshot symbol(long symbol) {
        this.symbol = symbol;
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
     * @return This instance of MarketDataSnapshot
     */
    public MarketDataSnapshot transactTime(long transactTime) {
        this.transactTime = transactTime;
        return this;
    }

    /**
     * Retrieves the bid price.
     *
     * @return A double value representing the bid price
     */
    public double bid() {
        return bid;
    }

    /**
     * Sets the bid price.
     *
     * @param bid A double value representing the bid price
     * @return This instance of MarketDataSnapshot
     */
    public MarketDataSnapshot bid(double bid) {
        this.bid = bid;
        return this;
    }

    /**
     * Retrieves the ask price.
     *
     * @return A double value representing the ask price
     */
    public double ask() {
        return ask;
    }

    /**
     * Sets the ask price.
     *
     * @param ask A double value representing the ask price
     * @return This instance of MarketDataSnapshot
     */
    public MarketDataSnapshot ask(double ask) {
        this.ask = ask;
        return this;
    }

    /**
     * Calculates and returns the spread between ask and bid prices.
     *
     * @return A double value representing the spread
     */
    public double spread() {
        return ask() - bid();
    }

    /**
     * Validates if the snapshot data is valid by checking if the bid and ask prices are not NaN.
     *
     * @return A boolean value representing the validity of the snapshot
     */
    public boolean valid() {
        return !(Double.isNaN(ask) || Double.isNaN(bid));
    }
}
