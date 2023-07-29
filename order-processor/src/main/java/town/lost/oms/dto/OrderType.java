/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;
/**
 * Enumeration for the types of orders in a trading system.
 *
 * <p>It contains two values: MARKET and LIMIT, which represent the two main types of orders that can be placed
 * in a trading system. A MARKET order is an order to buy or sell a security at the current market price,
 * whereas a LIMIT order is an order to buy or sell a security at a specific price or better.</p>
 *
 * <p>This enumeration can be used when creating trading orders, like so:</p>
 *
 * <pre>
 * NewOrderSingle nos = new NewOrderSingle()
 *    .sender(BASE85.parse("client"))
 *    .target(BASE85.parse("OMS"))
 *    .clOrdID("clOrdId")
 *    .orderQty(1e6)
 *    .price(1.6)
 *    .symbol(BASE85.parse("AUDUSD"))
 *    .ordType(OrderType.limit)
 *    .side(BuySell.buy);
 * </pre>
 *
 * <p>Note that the order type is indicated by the {@link OrderType} used in the 'ordType' field of the order.</p>
 */
public enum OrderType {
    /**
     * Market order type, which means the order should be executed at the current market price.
     */
    market,

    /**
     * Limit order type, which means the order should be executed at a specific price or better.
     */
    limit
}

