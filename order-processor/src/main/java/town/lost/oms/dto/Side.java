//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/*
 * Copyright 2016-2025 chronicle.software
 */

package town.lost.oms.dto;

/**
 * Enumeration for the direction of a trade order.
 *
 * <p>It contains two values: BUY and SELL, which represents the direction of the order. BUY (+1) means the order is to
 * purchase, while SELL (-1) implies the order is to sell.
 *
 * <p>This enumeration can be used to create trading orders, like so:
 *
 * <pre>{@code
 * NewOrderSingle nos = new NewOrderSingle()
 *    .sender(toLong("sender"))
 *    .target(toLong("target"))
 *    .transactTime(now())
 *    .sendingTime(now())
 *    .orderQty(1)
 *    .ordType(OrderType.MARKET)
 *    .side(Side.BUY)
 *    .symbol(toLong("EURUSD"));
 * }</pre>
 *
 * <p>Note that the direction is indicated by the {@link Side} used in the 'side' field of the order.
 */
public enum Side {
    /**
     * Buy order direction, represented by an integer value of +1.
     * Indicates an order to purchase.
     */
    BUY(+1),

    /**
     * Sell order direction, represented by an integer value of -1.
     * Indicates an order to sell.
     */
    SELL(-1);

    /**
     * The direction of the order.
     */
    public final int direction;

    /**
     * Constructs a BuySell enum with the specified direction.
     *
     * @param direction the direction of the order (+1 for buy, -1 for sell)
     */
    Side(int direction) {
        this.direction = direction;
    }

    /**
     * Gets the direction indicator of the order.
     *
     * @return the direction indicator as an integer
     */
    public int direction() {
        return direction;
    }
    }
