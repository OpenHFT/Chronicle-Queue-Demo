/*
 * Copyright (c) 2016-2022 Chronicle Software Ltd
 */

package org.trading.api;

import org.trading.dto.MarketDataIncrement;

public interface AggregatorIn {
    void mdi(MarketDataIncrement mdi);
}
