/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.dto;

public enum BuySell {
    buy(-1), sell(+1);

    public final int direction;

    BuySell(int direction) {
        this.direction = direction;
    }
}
