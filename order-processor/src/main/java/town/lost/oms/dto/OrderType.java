//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/*
 * Copyright 2016-2025 chronicle.software
 */

package town.lost.oms.dto;
/**
 * Enumeration for the types of orders in a trading system.
 *
 * <p>It contains two values: MARKET and LIMIT, which represent the two main types of orders that can be placed
 * in a trading system. A MARKET order is an order to buy or sell a security at the current market price,
 * whereas a LIMIT order is an order to buy or sell a security at a specific price or better.
 *
 * <p>This enumeration can be used when creating trading orders, like so:
 *
 * <pre>
 * NewOrderSingle nos = new NewOrderSingle()
 *    .sender(ShortText.parse("client"))
 *    .target(ShortText.parse("OMS"))
 *    .clOrdID("clOrdID")
 *    .orderQty(1e6)
 *    .price(1.6)
 *    .symbol(ShortText.parse("AUDUSD"))
 *    .ordType(OrderType.LIMIT)
 *    .side(BuySell.BUY);
 * </pre>
 *
 * <p>Note that the order type is indicated by the {@link OrderType} used in the 'ordType' field of the order.
 */
public enum OrderType {
    /**
     * Market order type, which means the order should be executed at the current market price.
     */
    MARKET,

    /**
     * Limit order type, which means the order should be executed at a specific price or better.
     */
    LIMIT,

    /**
     * Pegged order type, where the price is pegged to a benchmark price, such as the best bid or ask.
     */
    PEGGED,

    /**
     * Fill or Kill order type, which must be executed immediately in its entirety or cancelled.
     */
    FILL_OR_KILL,

    /**
     * Immediate or Cancel order type, which executes all or part immediately and cancels any unfilled portion.
     */
    IMMEDIATE_OR_CANCEL,
}

