/*
 * Copyright (c) 2016-2019 Chronicle Software Ltd
 */

package town.lost.oms.dto;
/**
 * Enumeration for the direction of a trade order.
 *
 * <p>It contains two values: BUY and SELL, which represents the direction of the order. BUY (+1) means the order is to
 * purchase, while SELL (-1) implies the order is to sell.</p>
 *
 * <p>This enumeration can be used to create trading orders, like so:</p>
 *
 * <pre>
 * NewOrderSingle nos = new NewOrderSingle()
 *    .sender(toLong("sender"))
 *    .target(toLong("target"))
 *    .transactTime(now())
 *    .sendingTime(now())
 *    .orderQty(1)
 *    .ordType(OrderType.market)
 *    .side(BuySell.buy)
 *    .symbol(toLong("EURUSD"));
 * </pre>
 *
 * <p>Note that the direction is indicated by the {@link BuySell} used in the 'side' field of the order.</p>
 */
public enum BuySell {
    /**
     * Buy order direction, represented by an integer value of +1.
     */
    buy(+1),

    /**
     * Sell order direction, represented by an integer value of -1.
     */
    sell(-1);

    /**
     * The direction of the order.
     */
    public final int direction;

    /**
     * Constructs a BuySell enum with the specified direction.
     *
     * @param direction the direction of the order (+1 for buy, -1 for sell)
     */
    BuySell(int direction) {
        this.direction = direction;
    }
}
