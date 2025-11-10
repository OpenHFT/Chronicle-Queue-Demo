//
// Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
//

/*
 * Copyright 2016-2025 chronicle.software
 */

package org.trading.dto;

/**
 * This is a BuySell enumeration representing the two possible sides of a trading order: Buy or Sell.
 * Each side is associated with a direction, with Buy being -1 and Sell being +1.
 */
public enum BuySell {
    // Buy side of the trade, represented with a direction of -1
    buy(-1),

    // Sell side of the trade, represented with a direction of +1
    sell(+1);

    // The direction associated with the side of the trade
    public final int direction;

    /**
     * Constructor for the BuySell enumeration, initializing the direction.
     *
     * @param direction An int representing the direction of the trade, where -1 is Buy and +1 is Sell
     */
    BuySell(int direction) {
        this.direction = direction;
    }
}
